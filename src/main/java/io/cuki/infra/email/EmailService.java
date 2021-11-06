package io.cuki.infra.email;

import io.cuki.infra.email.exception.IncorrectVerificationCodeException;
import io.cuki.infra.email.exception.SendMailFailedException;
import io.cuki.infra.email.exception.VerificationCodeExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.util.Objects;
import java.util.Random;

@Slf4j
@Service
@PropertySource("classpath:email.properties")
public class EmailService {

    private final JavaMailSender mailSender;
    private final RedisTemplate<String, Object> redisTemplate;

    private final String SENDER_EMAIL;
    private final String SENDER_NAME = "cuki";
    private final String KEY_PREFIX = "email:";
    private final int LIMIT_TIME = 60 * 5;

    public EmailService(JavaMailSender mailSender, RedisTemplate<String, Object> redisTemplate, @Value("${AdminMail.id}") String SENDER_EMAIL) {
        this.mailSender = mailSender;
        this.redisTemplate = redisTemplate;
        this.SENDER_EMAIL = SENDER_EMAIL;
    }

    // 인증번호 발송 - 회원가입
    @Async
    public void sendMessageForSignUp(String email) {
        String verificationCode = createVerificationCode();
        MimeMessage message = createMessageForSignUp(email, verificationCode);

        log.debug("관리자 계정: {}", SENDER_EMAIL);
        log.debug("보내는 대상: {}", email);
        log.debug("인증 번호: {}", verificationCode);

        try {
            // 1. 레디스 서버에 인증번호 저장
            redisTemplate.opsForValue().set(KEY_PREFIX + email, verificationCode, Duration.ofSeconds(LIMIT_TIME));
            // 2. 메일 전송
            mailSender.send(message);
        } catch (MailException e){
            e.printStackTrace();
            log.error("메일 전송에 실패했습니다.");
            throw new SendMailFailedException("메일 전송에 실패했습니다.");
        }
    }

    // 인증번호 발송 - 로그인
    @Async
    public void sendMessageForLogin(String email) {
        String verificationCode = createVerificationCode();
        MimeMessage message = createMessageForLogin(email, verificationCode);

        log.debug("관리자 계정: {}", SENDER_EMAIL);
        log.debug("보내는 대상: {}", email);
        log.debug("인증 번호: {}", verificationCode);

        try {
            // 1. 레디스 서버에 인증번호 저장
            redisTemplate.opsForValue().set(KEY_PREFIX + email, verificationCode, Duration.ofSeconds(LIMIT_TIME));
            // 2. 메일 전송
            mailSender.send(message);
        } catch (MailException e){
            e.printStackTrace();
            log.error("메일 전송에 실패했습니다.");
            throw new SendMailFailedException("메일 전송에 실패했습니다.");
        }
    }

    // MimeMessage - 회원가입
    private MimeMessage createMessageForSignUp(String email, String verificationCode) {
        verificationCode = createVerificationCode();
        MimeMessage message = mailSender.createMimeMessage();
        try {
            message.addRecipients(Message.RecipientType.TO, email); // 보내는 대상
            message.setSubject("[cuki] 회원가입을 위한 인증번호를 확인해주세요.");  // 제목

            String html = "";
            html += "<div align='center' style='margin:50px;'>";
            html += "<h1>가입 안내</h1>";
            html += "<br>";
            html += "<p>아래 인증 코드를 입력하시면 회원가입이 완료됩니다. 감사합니다.</p>";
            html += "<br>";
            html += "<div align='center' style='border:1px solid black; font-family:verdana';>";
            html += "<h3 style='color:darkgreen;'>회원가입 코드</h3>";
            html += "<div style='font-size:130%'>";
            html += "CODE : <strong>";
            html += verificationCode;
            html += "</strong><div><br/> ";
            html += "<a href='https://cuki.io' target='_blank'>cuki 홈페이지 바로가기</a>";
            html += "</div>";

            message.setText(html, "utf-8", "html");  // 내용
            message.setFrom(new InternetAddress(SENDER_EMAIL, SENDER_NAME)); // 보내는 사람
        } catch (UnsupportedEncodingException | MessagingException e) {
            e.printStackTrace();
            throw new SendMailFailedException("메일 전송에 실패했습니다.");
        }

        return message;
    }

    // MimeMessage - 로그인
    private MimeMessage createMessageForLogin(String email, String verificationCode){
        verificationCode = createVerificationCode();
        MimeMessage message = mailSender.createMimeMessage();
        try {
            message.addRecipients(Message.RecipientType.TO, email); // 보내는 대상
            message.setSubject("[cuki] 로그인을 위한 인증번호를 확인해주세요.");  // 제목

            String html = "";
            html += "<div align='center' style='margin:50px;'>";
            html += "<h1>로그인 안내</h1>";
            html += "<br>";
            html += "<p>아래 인증 코드를 입력하시면 로그인이 완료됩니다. 감사합니다.</p>";
            html += "<br>";
            html += "<div align='center' style='border:1px solid black; font-family:verdana';>";
            html += "<h3 style='color:darkgreen;'>로그인 코드</h3>";
            html += "<div style='font-size:130%'>";
            html += "CODE : <strong>";
            html += verificationCode;
            html += "</strong><div><br/> ";
            html += "<a href='https://cuki.io' target='_blank'>cuki 홈페이지 바로가기</a>";
            html += "</div>";

            message.setText(html, "utf-8", "html");  // 내용
            message.setFrom(new InternetAddress(SENDER_EMAIL, SENDER_NAME)); // 보내는 사람
        } catch (UnsupportedEncodingException | MessagingException e) {
            e.printStackTrace();
            throw new SendMailFailedException("메일 전송에 실패했습니다.");
        }

        return message;
    }

    // 인증코드 만들기
    private String createVerificationCode() {

        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {   // 인증코드 8자리
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }

    // 인증코드 검사
    public void verifyCode(String email, String uncheckedCode) {

        String actualCode = (String) redisTemplate.opsForValue().get(KEY_PREFIX + email);

        log.debug("uncheckedCode : " + uncheckedCode + " actualCode : " + actualCode);
        log.debug("email : " + email);


        if (!Boolean.TRUE.equals(redisTemplate.opsForValue().getOperations().hasKey(KEY_PREFIX + email))) {
            throw new VerificationCodeExpiredException("인증번호 입력시간이 만료되었거나 유효하지 않은 요청입니다.");
        }

        if (Objects.equals(actualCode, uncheckedCode)) {
            redisTemplate.delete(KEY_PREFIX + email);
        } else {
            throw new IncorrectVerificationCodeException();
        }
    }
}
