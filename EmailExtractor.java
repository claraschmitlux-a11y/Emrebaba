package com.emailextractor;

import android.content.Context;
import android.os.Environment;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class EmailExtractor {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}",
        Pattern.CASE_INSENSITIVE
    );

    public static String[] extractEmails(String text) {
        java.util.regex.Matcher matcher = EMAIL_PATTERN.matcher(text);
        java.util.List<String> emails = new java.util.ArrayList<>();
        
        while (matcher.find()) {
            String email = matcher.group();
            if (!emails.contains(email)) {
                emails.add(email);
            }
        }
        
        return emails.toArray(new String[0]);
    }

    public static boolean saveEmailsToFile(Context context, String[] emails) {
        try {
            File directory = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            if (directory != null && directory.exists()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault());
                String timestamp = sdf.format(new Date());
                String fileName = "extracted_emails_" + timestamp + ".txt";
                File file = new File(directory, fileName);

                FileWriter writer = new FileWriter(file);
                writer.write("E-posta Ayıklama Raporu\n");
                writer.write("Tarih: " + new Date() + "\n");
                writer.write("Toplam e-posta: " + emails.length + "\n");
                writer.write("==================================================\n\n");

                for (int i = 0; i < emails.length; i++) {
                    writer.write((i + 1) + ". " + emails[i] + "\n");
                }

                writer.write("\n==================================================\n");
                writer.write("Dosya: " + file.getName() + "\n");
                writer.write("Konum: " + file.getAbsolutePath() + "\n");
                writer.close();

                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public static String[] filterValidEmails(String[] emails) {
        java.util.List<String> validEmails = new java.util.ArrayList<>();
        for (String email : emails) {
            if (isValidEmail(email)) {
                validEmails.add(email);
            }
        }
        return validEmails.toArray(new String[0]);
    }
}