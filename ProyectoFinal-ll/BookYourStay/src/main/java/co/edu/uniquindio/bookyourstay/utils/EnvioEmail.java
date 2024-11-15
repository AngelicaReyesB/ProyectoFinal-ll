package co.edu.uniquindio.bookyourstay.utils;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class EnvioEmail {
    private String destinatario;
    private String asunto;
    private String mensaje;

    public EnvioEmail(String destinatario, String asunto, String mensaje) {
        this.destinatario = destinatario;
        this.asunto = asunto;
        this.mensaje = mensaje;
    }

    private Session crearSesion() {
        final String username = "clinicauq@gmail.com"; // Dirección de correo Gmail
        final String appPassword = "tu_contraseña_de_aplicación"; // Aquí va la contraseña de aplicación, generada en la cuenta de Google

        // Configuración del servidor SMTP de Gmail
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Autenticación utilizando una clase anónima
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, appPassword);
            }
        };

        return Session.getInstance(props, authenticator);
    }

    public void enviarNotificacion() {
        Session session = crearSesion();
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("no-reply@email.com")); // Remitente del correo
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto);
            message.setText(mensaje);
            Transport.send(message);
        } catch (MessagingException e) {
            System.out.println("Error de envío de correo: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al enviar el correo electrónico", e);
        }
    }
}
