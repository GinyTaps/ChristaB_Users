package com.bafagroupe.christab_security.service;

import com.bafagroupe.christab_security.dao.AppRoleRepository;
import com.bafagroupe.christab_security.dao.AppUserRepository;
import com.bafagroupe.christab_security.dao.ConfirmationTokenRepository;
import com.bafagroupe.christab_security.dto.*;
import com.bafagroupe.christab_security.entities.AppRole;
import com.bafagroupe.christab_security.entities.AppUser;
import com.bafagroupe.christab_security.entities.ConfirmationToken;
import com.bafagroupe.christab_security.mappers.AppRoleMapper;
import com.bafagroupe.christab_security.mappers.AppUserMapper;
import com.bafagroupe.christab_security.openFeign.UtilisateurRestClient;
import com.bafagroupe.christab_security.security.SecurityParams;
import com.bafagroupe.christab_security.web.util.FonctionsUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Transactional
@Service
@AllArgsConstructor
public class AccountServiceI implements AccountService {

    private AppUserRepository appUserRepository;
    private AppRoleRepository appRoleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private ConfirmationTokenRepository confirmationTokenRepository;
    private AppUserMapper appUserMapper;
    private AppRoleMapper appRoleMapper;
    private UtilisateurRestClient utilisateurRestClient;
    private FonctionsUtil fu;


    /****************** ==== Enregistrement ==== ***************/
    public AppUserResponseDto saveUser(AppUserRequestDto appUserRequestDto) {
        AppUser user = appUserRepository.findUByEmail(appUserRequestDto.getEmail());

        if(user != null) throw new RuntimeException("User already exists");
        if(!appUserRequestDto.getPassword().equals(appUserRequestDto.getPasswordConfirmed())) throw new RuntimeException("Please confirm password");
        AppUser appUser = new AppUser();
        appUser.setEmail(appUserRequestDto.getEmail());
        appUser.setPassword(bCryptPasswordEncoder.encode(appUserRequestDto.getEmail()));

        appUserRepository.save(appUser);
        UserCustomRequestDto userCustomRequestDto = appUserMapper.userRequestDtoToUserCustom(appUserRequestDto);
        // addRoleToUser(appUserRequestDto.getEmail(), "USER");
        userCustomRequestDto.setRolename("USER");
        addRoleToUser(userCustomRequestDto);
        AppUserResponseDto  appUserResponseDto = appUserMapper.appuserToAppUserDto(appUser);

        return appUserResponseDto;
    }

    /****************** ==== Fonction supplémentaire (Activation du compte d'un utilisateur) ==== ***************/
    @Override
    public AppUserResponseDto activatedUser(AppUserRequestDto appUserRequestDto) {
        AppUser updUser = appUserRepository.findUByEmail(appUserRequestDto.getEmail());
        if(updUser != null) {
            System.out.println(updUser.getEmail());
            updUser.setValidated(true);
        }


        ConfirmationToken confirmationToken = new ConfirmationToken(updUser);
        confirmationTokenRepository.save(confirmationToken);

        Properties properties = new Properties();
        properties.put("mail.smtp.host", SecurityParams.HOST);
        properties.put("mail.smtp.port", SecurityParams.PORT);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SecurityParams.USERNAME, SecurityParams.PASSWORD);
            }
        };
        Session session = Session.getDefaultInstance(properties, authenticator);
        try {

            Message mailMessage = new MimeMessage(session);
            mailMessage.setFrom(new InternetAddress(SecurityParams.USERNAME));
            mailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(updUser.getEmail()));
            mailMessage.setSubject("Activation du compte ChristaB!");
            mailMessage.setText(SecurityParams.EMAIL_CONTENT+confirmationToken.getConfirmationToken());

            Transport.send(mailMessage);
            System.out.println("Mail de confirmation de l'email envoyé");

            appUserRepository.save(updUser);
            System.out.println("Mail d'activation envoyé!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return appUserMapper.appuserToAppUserDto(updUser);
    }


    @Override
    public Boolean checkUserByEmail(String email) {
        boolean result;
        AppUser user = appUserRepository.findUByEmail(email);
        if(user != null) {
            result =  true;
        }
        else {
            result =  false;
        }
        return result;
    }

    @Override
    public Boolean checkUserByEmailAndUtilisateur(String email) {
        boolean result;
        AppUser user = appUserRepository.findUByEmail(email);
        boolean response = utilisateurRestClient.checkUtilisateurByEmail(email);
        System.out.println(response);
        if(user != null && response) {
            result =  true;
        }
        else {
            result =  false;
        }
        return result;
    }

    /****************** ==== Enregistrement avec vérification de l'email ==== ***************/

    /****************** ==== Confirmation du compte ==== ***************/
    @Override
    public String confirmUserAccount(String confirmationToken) throws Exception {

        final RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        if(token == null) throw new Exception("Erreur: Le lien est invalide ou interrompu!");
        AppUser user = appUserRepository.findUByEmail(token.getUser().getEmail());
        user.setActivated(true);
        appUserRepository.save(user);
        System.out.println("Compte vérifié avec succès");
        redirectAttributes.addFlashAttribute("Vérification du compte", user.getEmail()+" résussi");

        return SecurityParams.ACTIVATED_ACCOUNT_MESSAGE+"Compte de "+user.getEmail()+" vérifié avec succès!";
    }

    /****************** ==== Affichage d'un utilisateur  ==== ***************/
    @Override
    public AppUserResponseDto loadUserByEmail(String email) {
        AppUser appUser = appUserRepository.findUByEmail(email);
        return appUserMapper.appuserToAppUserDto(appUser);
    }

    public AppUser loadUserByEmailF(String email) {
        return appUserRepository.findByEmail(email);
    }


    /****************** ==== Enregistrement d'un droit  ==== ***************/
    @Override
    public AppRoleResponseDto saveRole(AppRoleRequestDto appRoleRequestDto) {
        AppRole appRole = appRoleMapper.appRoleRequestDtoToAppRole(appRoleRequestDto);
        appRoleRepository.save(appRole);
        return appRoleMapper.appRoleToAppRoleDto(appRole);
    }

    /****************** ==== Attribution d'un droit à un utilisateur  ==== ***************/
    @Override
    public void addRoleToUser(UserCustomRequestDto userCustomRequestDto) {
        AppUser appUser = appUserRepository.findUByEmail(userCustomRequestDto.getEmail());
        AppRole appRole = appRoleRepository.findByRolename(userCustomRequestDto.getRolename());
        appUser.getRoles().add(appRole);
    }


/*********************************** Ré-initialisation du mot de passe d'un utilisateur ****************************/
    @Override
    public boolean recupPassword(String email) {
        SecurityParams.emailRecup = email;
        int code;

        code = fu.getRandomDigitCode();
        AppUser user = appUserRepository.findUByEmail(email);


        // permet de vérifier si l'email existe vraiment
        if(user.getEmail().equals(email)) {


            Properties prop = new Properties();
            prop.put("mail.smtp.host", SecurityParams.HOST);
            prop.put("mail.smtp.port",SecurityParams.PORT);
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.starttls.enable", "true"); //TLS

            Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SecurityParams.USERNAME, SecurityParams.PASSWORD); // SecurityParams.PASS);
                }
            });

            try {

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(SecurityParams.USERNAME));
                message.setRecipients(
                        Message.RecipientType.TO,
                        InternetAddress.parse(email)
                );
                message.setSubject("Code de ré-initialisation du mot de passe");
                message.setText(SecurityParams.MESSAGE +code );

                Transport.send(message);
                System.out.println("Code envoyé dans le mail");

                user.setPasswordForget(code);
                user.setLimitPasswordTime(fu.getCurrentDateTime());
                appUserRepository.save(user);
                // System.out.println(code);

            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return true;
        }
        else {
            // System.out.println("Email non valide");
            return false;
        }
    }

    /****************** ==== Vérification du code envoyé ==== ***************/
    @Override
    public boolean verifCode(String email, int code) {

        AppUser user = appUserRepository.findUByEmail(email);

        if( (code == user.getPasswordForget()) && (fu.getCurrentDateTime().compareTo(user.getLimitPasswordTime()) < 30*60*1000 ) ) {
            // System.out.println("code valide");
            return true;
        }
        else {
            // System.out.println("code invalide");
            return false;
        }
    }

    /****************** ==== Ré-initialisation du mot de passe ==== ***************/
    @Override
    public AppUserResponseDto reInitPassword(UserFormRequestDto userFormRequestDto) {
        AppUser user = appUserRepository.findUByEmail(userFormRequestDto.getEmail());
        // permet de vérifier si l'email existe vraiment
        if(user.getEmail().equals(userFormRequestDto.getEmail()) && verifCode(userFormRequestDto.getEmail(), userFormRequestDto.getCode())) {
            if(!userFormRequestDto.getPassword().equals(userFormRequestDto.getPasswordConfirmed())) throw new RuntimeException("Please confirm password");
            user.setPassword(bCryptPasswordEncoder.encode(userFormRequestDto.getPassword()));
            appUserRepository.save(user);
            // System.out.println("*********** L'enregistrement du mot de passe de l'utilisateur: "+user);
            // return user;
        }
        return appUserMapper.appuserToAppUserDto(user);
    }




/*************************************************** Fonctions supplémentaire ********************************************/


    /******************* ==== Réactivation du compte après modification ==== **************/
    @Override
    public AppUserResponseDto reactivatedUser(AppUserRequestDto appUserRequestDto) {
        AppUser updUser = appUserRepository.findUByEmail(appUserRequestDto.getEmail());


        ConfirmationToken confirmationToken = new ConfirmationToken(updUser);
        confirmationTokenRepository.save(confirmationToken);

        Properties properties = new Properties();
        properties.put("mail.smtp.host", SecurityParams.HOST);
        properties.put("mail.smtp.port", SecurityParams.PORT);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SecurityParams.USERNAME, SecurityParams.PASSWORD);
            }
        };
        Session session = Session.getDefaultInstance(properties, authenticator);
        try {

            Message mailMessage = new MimeMessage(session);
            mailMessage.setFrom(new InternetAddress(SecurityParams.USERNAME));
            mailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(updUser.getEmail()));
            mailMessage.setSubject("Vérification du nouvel email!");
            mailMessage.setText(SecurityParams.EMAIL_REACTIVATE+confirmationToken.getConfirmationToken());

            Transport.send(mailMessage);
            System.out.println("Mail de confirmation de l'email envoyé");

            appUserRepository.save(updUser);
            System.out.println("Mail d'activation envoyé!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return appUserMapper.appuserToAppUserDto(updUser);
    }


    /****************** ==== Désactivation du compte d'un utilisateur ==== ***************/
    @Override
    public AppUserResponseDto deactivatedUser(AppUserRequestDto appUserRequestDto) {
        AppUser updUser = appUserRepository.findUByEmail(appUserRequestDto.getEmail());
        updUser.setActivated(false);
        appUserRepository.save(updUser);

        return appUserMapper.appuserToAppUserDto(updUser);
    }

    /*=============*/
    @Override
    public void disableUser(AppUserRequestDto appUserRequestDto) {
        AppUser user = appUserRepository.findUByEmail(appUserRequestDto.getEmail());
        if(user != null) {
            user.setActivated(false);
        }
    }

    /****************** ==== Modification du mot de passe d'un utilisateur ==== ***************/
    @Override
    public AppUserResponseDto updateUserPassword(UserRequestDto userRequestDto) {
        AppUser updUser = appUserRepository.findUByEmail(userRequestDto.getEmailOld());

        if(updUser != null) {
            if(!bCryptPasswordEncoder.matches(userRequestDto.getOldPassword(), updUser.getPassword())) throw new RuntimeException("Ancien mot de passe incorrecte");
            if(!userRequestDto.getPassword().equals(userRequestDto.getPasswordConfirmed())) throw new RuntimeException("Les mots de passe ne correspondent pas");
            updUser.setEmail(userRequestDto.getEmail());
            updUser.setPassword(bCryptPasswordEncoder.encode(userRequestDto.getPassword()));
            appUserRepository.save(updUser);
        }

        return appUserMapper.appuserToAppUserDto(updUser);
    }


    /****************** ==== Modification du compte  d'un utilisateur et désactivation temporaire du compte pour vérifier le nouvel email ==== ***************/
    @Override
    public AppUserResponseDto updateUser(AppUserRequestDto appUserRequestDto) {
        AppUser updUser = appUserRepository.findUByEmail(appUserRequestDto.getEmailOld());
        updUser.setEmail(appUserRequestDto.getEmail());
        updUser.setActivated(false);
        appUserRepository.save(updUser);
        AppUserRequestDto  userRequestDto = appUserMapper.appUserToAppserRDto(updUser);
        reactivatedUser(userRequestDto);

        return appUserMapper.appuserToAppUserDto(updUser);
    }

    /****************** ==== Activation du droit d'un utilisateur ==== ***************/
    @Override
    public AppRoleResponseDto updateRole(AppRoleRequestDto appRoleRequestDto) {
        AppRole updR = appRoleRepository.findOneById(appRoleRequestDto.getIdAppRole());
        updR.setRolename(appRoleRequestDto.getRolename());
        appRoleRepository.save(updR);
        return appRoleMapper.appRoleToAppRoleDto(updR);
    }

    /****************** ==== Suppression du compte d'un utilisateur ==== ***************/
    @Override
    public void deleteUser(AppUserRequestDto appUserRequestDto) {
        deleteUserRole(appUserRequestDto);
        AppUser appUser = appUserMapper.appUserRequestDtoToAppUser(appUserRequestDto);
        appUserRepository.delete(appUser);
    }


    @org.springframework.transaction.annotation.Transactional
    @Override
    public void deleteUserRole(AppUserRequestDto appUserRequestDto) {
        appUserRepository.deleteAppUserRoles(appUserRequestDto.getEmail());
    }


    @Override
    public void deleteRole(Long id) {
        appRoleRepository.deleteOneById(id);
    }

    @Override
    public AppRoleResponseDto loadRole(long id) {
        AppRole appRole = appRoleRepository.findOneById(id);
        return appRoleMapper.appRoleToAppRoleDto(appRole);
    }


    @Override
    public AppUserResponseDto loadUser(long id) {
        AppUser appUser = appUserRepository.findOneById(id);
        return appUserMapper.appuserToAppUserDto(appUser);
    }


    @Override
    public List<AppRoleResponseDto> loadRoles() {
        List<AppRole> appRoles = appRoleRepository.findAll();
        List<AppRoleResponseDto> appRoleResponseDtos = appRoles.stream()
                .map(appRole -> appRoleMapper.appRoleToAppRoleDto(appRole))
                .collect(Collectors.toList());
        return appRoleResponseDtos;
    }


    @Override
    public List<AppUserResponseDto> loadUsers() {
        List<AppUser> appUsers = appUserRepository.findAll();
        List<AppUserResponseDto> appUserResponseDtos = appUsers.stream()
                .map(appUser -> appUserMapper.appuserToAppUserDto(appUser))
                .collect(Collectors.toList());
        return appUserResponseDtos;
    }

}
