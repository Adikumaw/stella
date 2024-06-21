package com.nothing.ecommerce.miscellaneous;

public class EmailTemplate {

        public static final String EMAIL_VERIFICATION_TEMPLATE = """
                        Hi %s,
                        Thanks for signing up for %s!
                        To complete your registration and access all the features, please verify your email address.
                        
                        Click the button below to verify your email:
                        [Verify Email Button](%s)
                        
                        OR
                        
                        If you can't click the button, copy and paste the following link into your web browser:
                        %s
                        
                        This link will expire in %d hours.\s
                        
                        Why Verify Your Email?
                        Verifying your email address helps us keep your account secure and ensures you receive important updates from %s.
                        
                        Having Trouble?
                        If you're having trouble verifying your email address, please contact us at this email.
                        
                        Thanks,
                        The %s Team\
                        """;

        public static final String EMAIL_UPDATE_VERIFICATION_TEMPLATE = """
                        Hi %s,
                        You recently updated your details on %s. To ensure the accuracy of your information and the security of your account, please verify these changes.
                        
                        Updated Details:
                        * %s
                        
                        Click the button below to confirm your updated details:
                        [Verify Details Button](%s)
                        
                        OR
                        
                        If you can't click the button, copy and paste the following link into your web browser:
                        %s
                        
                        This link will expire in %d hours.\s
                        
                        Didn't Make These Changes?
                        If you did not request these updates, please contact us immediately at %s to secure your account.
                        
                        Thanks,
                        The %s Team\
                        """;
}
