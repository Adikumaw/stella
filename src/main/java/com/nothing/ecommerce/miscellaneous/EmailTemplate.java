package com.nothing.ecommerce.miscellaneous;

public class EmailTemplate {

        public static final String EMAIL_VERIFICATION_TEMPLATE = "Hi %s,\n" +
                        "Thanks for signing up for %s!\n" +
                        "To complete your registration and access all the features, please verify your email address.\n\n"
                        +
                        "Click the button below to verify your email:\n" +
                        "[Verify Email Button](%s)\n\n" +
                        "OR\n\n" +
                        "If you can't click the button, copy and paste the following link into your web browser:\n" +
                        "%s\n\n" +
                        "This link will expire in %d hours. \n\n" +
                        "Why Verify Your Email?\n" +
                        "Verifying your email address helps us keep your account secure and ensures you receive important updates from %s.\n\n"
                        +
                        "Having Trouble?\n" +
                        "If you're having trouble verifying your email address, please contact us at this email.\n\n" +
                        "Thanks,\n" +
                        "The %s Team";

        public static final String EMAIL_UPDATE_VERIFICATION_TEMPLATE = "Hi %s,\n" +
                        "You recently updated your details on %s. To ensure the accuracy of your information and the security of your account, please verify these changes.\n\n"
                        +
                        "Updated Details:\n" +
                        "* %s\n\n" + // This line will be repeated for each updated detail
                        "Click the button below to confirm your updated details:\n" +
                        "[Verify Details Button](%s)\n\n" +
                        "OR\n\n" +
                        "If you can't click the button, copy and paste the following link into your web browser:\n" +
                        "%s\n\n" +
                        "This link will expire in %d hours. \n\n" +
                        "Didn't Make These Changes?\n" +
                        "If you did not request these updates, please contact us immediately at %s to secure your account.\n\n"
                        +
                        "Thanks,\n" +
                        "The %s Team";
}
