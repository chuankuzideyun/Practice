package com.practice;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ATM {
    private ArrayList<Account> accounts = new ArrayList<>();

    private Scanner sc = new Scanner(System.in);

    public void start() {
        while (true) {
            System.out.println("Welcom!");
            System.out.println("1. Log in");
            System.out.println("2. Create account");
            System.out.println("Please choose 1 or 2:");

            int command = sc.nextInt();
            switch (command) {
                case 1:
                    break;
                case 2:
                    createAccount();
                    break;
                default:
                    System.out.println("Wrong number");
            }
        }
    }

    private void createAccount() {
        Account acc = new Account();
        System.out.println("Please input your account name:");
        String name = sc.next();
        acc.setUserName(name);

        while (true) {
            System.out.println("Please input your gender: (F/M)");
            char sex = sc.next().charAt(0);
            if (sex == 'F' || sex == 'M') {
                acc.setSex(sex);
                break;
            } else {
                System.out.println("There is an error in entering your gender, please re-enter.");
            }
        }

        while (true) {
            System.out.println("Please input your password:");
            String passWord = sc.next();
            System.out.println("Please input your password again.");
            String okPasssWord = sc.next();
            if (okPasssWord.equals(passWord)) {
                acc.setPassWord(okPasssWord);
                break;
            } else {
                System.out.println("Two passwords do not match, please re-enter.");
            }
        }

        System.out.println("Please enter your cash withdrawal amount:");
        double limit = sc.nextDouble();
        acc.setLimit(limit);

        String newCardId = createCardId();
        acc.setCardId(newCardId);

        accounts.add(acc);
        System.out.println("Congratulations," + acc.getUserName() + "the account has been successfully opened."
                + "Your card ID is" + acc.getCardId() + ".");
    }

    private String createCardId() {
        while (true) {
            String cardId = "";
            Random r = new Random();
            for (int i = 0; i < 8; i++) {
                int data = r.nextInt(10);
                cardId += data;
            }
            Account acc = getAccountById(cardId);
            if (acc == null) {
                return cardId;
            }
        }
    }

    private Account getAccountById(String cardId) {
        for (int i = 0; i < accounts.size(); i++) {
            Account acc = accounts.get(i);
            if (acc.getCardId().equals(cardId)) {
                return acc;
            }
        }
        return null;
    }
}
