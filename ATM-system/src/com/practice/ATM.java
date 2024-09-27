package com.practice;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.xml.crypto.dsig.TransformService;

public class ATM {
    private ArrayList<Account> accounts = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);
    private Account loginAcc;

    public void start() {
        while (true) {
            System.out.println("Welcom!");
            System.out.println("1. Log in");
            System.out.println("2. Create account");
            System.out.println("Please choose 1 or 2:");

            int command = sc.nextInt();
            switch (command) {
                case 1:
                    login();
                    break;
                case 2:
                    createAccount();
                    break;
                default:
                    System.out.println("Wrong number");
            }
        }
    }

    private void login() {
        System.out.println("== Login the system ==");

        if (accounts.size() == 0) {
            System.out.println(
                    "Currently there are no accounts in the system, please choose to create an account first.");
            return;
        }

        while (true) {
            System.out.println("Please input your card id:");
            String cardId = sc.nextLine();
            Account acc = getAccountById(cardId);
            if (acc == null) {
                System.out.println("Wrong card id.");
            } else {
                while (true) {
                    System.out.println("Please input your password:");
                    String password = sc.next();
                    if (acc.getPassWord().equals(password)) {
                        loginAcc = acc;
                        System.out.println("Congratulations!" + acc.getUserName()
                                + ", log in the system, your card id is:" + acc.getCardId());
                        // 登陆后界面
                        showUserCommand();
                        return;
                    } else {
                        System.out.println("Wrong password.");
                    }
                }
            }
        }

    }

    private void showUserCommand() {
        while (true) {
            System.out.println(loginAcc.getUserName() + "Please select a follow-up action:");
            System.out.println("1. Check account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfor");
            System.out.println("5. Change password");
            System.out.println("6. Log out");
            System.out.println("7. Cancel current account");
            int command = sc.nextInt();
            switch (command) {
                case 1:
                    showLoginAccount();
                    break;
                case 2:
                    depositMoney();
                    break;
                case 3:
                    drawMoney();
                    break;
                case 4:
                    transferMoney();
                    break;
                case 5:
                    updatePassword();
                    return;
                case 6:
                    System.out.println(loginAcc.getUserName() + ", log out of the system successfully.");
                    return;
                case 7:
                    if (deleteAccount()) {
                        return;
                    }
                    ;
                    break;
                default:
                    System.out.println("Current option does not exist.");
            }
        }
    }

    private void updatePassword() {
        System.out.println("==Change==");
        while (true) {
            System.out.println("Please enter your current password:");
            String password = sc.next();
            if (loginAcc.getPassWord() == password) {
                while (true) {
                    System.out.println("Please enter a new password:");
                    String newPass = sc.next();
                    System.out.println("Please enter your confirmation password:");
                    String conPass = sc.next();
                    if (newPass == conPass) {
                        loginAcc.setPassWord(newPass);
                        System.out.println("The password is successfully changed.");
                        return;
                    } else {
                        System.out.println("The password is inconsistent twice.");
                    }
                }
            } else {
                System.out.println("Wrong Password.");
            }
        }
    }

    private boolean deleteAccount() {
        System.out.println("==Delete==");
        System.out.println("Are you sure you want to delete your account?");
        String command = sc.next();
        switch (command) {
            case "y":
                if (loginAcc.getMoney() == 0) {
                    accounts.remove(loginAcc);
                    System.out.println("The account is successfully deleted.");
                    return true;
                } else {
                    System.out.println("There is an amount of money in the account, and the account cannot be closed.");
                    return false;
                }
            default:
                System.out.println("Your account has been saved.");
                return false;
        }
    }

    private void transferMoney() {
        System.out.println("==Transfer==");
        if (accounts.size() < 2) {
            System.out.println("Only one account in the system. The transfer operation cannot be performed");
            return;
        }
        if (loginAcc.getMoney() == 0) {
            System.out.println("You don't have money in the account.");
            return;
        }
        while (true) {
            System.out.println("Please enter the other person's card number:");
            String cardId = sc.next();
            Account acc = getAccountById(cardId);
            if (acc == null) {
                System.out.println("The account does not exist.");
            } else {
                String name = "*" + acc.getUserName().substring(1);
                System.out.println("Please fill in [" + name + "]'s last name");
                String preName = sc.next();
                if (acc.getUserName().startsWith(preName)) {
                    while (true) {
                        System.out.println("Please enter the amount to be transferred:");
                        double money = sc.nextDouble();
                        if (loginAcc.getMoney() >= money) {
                            loginAcc.setMoney(loginAcc.getMoney() - money);
                            acc.setMoney(acc.getMoney() + money);
                            System.out.println("The transfer is successful.");
                            return;
                        } else {
                            System.out.println("The balance is insufficient.");
                        }
                    }
                } else {
                    System.out.println("The certification is not qualified, and the last name is wrong.");
                }
            }
        }
    }

    private void drawMoney() {
        System.out.println("==Withdraw==");
        if (loginAcc.getMoney() < 100) {
            System.out.println("The account balance is insufficient.");
            return;
        }

        while (true) {
            System.out.println("Please enter the withdrawal amount");
            double money = sc.nextDouble();
            if (loginAcc.getMoney() >= money) {
                if (money > loginAcc.getLimit()) {
                    System.out.println(
                            "Your withdrawal amount exceeds the withdrawal limit, and your withdrawal limit is: "
                                    + loginAcc.getLimit());
                } else {
                    loginAcc.setMoney(loginAcc.getMoney() - money);
                    System.out.println(
                            "The withdrawal is successful, and the balance in the card is: " + loginAcc.getMoney());
                    break;
                }
            } else {
                System.out.println("The account balance is insufficient. Your balance is:" + loginAcc.getMoney());
            }
        }
    }

    private void depositMoney() {
        System.out.println("==Deposit==");
        System.out.println("Please enter an amount:");
        Double money = sc.nextDouble();// 默认金额没问题
        loginAcc.setMoney(loginAcc.getMoney() + money);
        System.out.println(
                "If the deposit is successful, the total amount in the current card is: " + loginAcc.getMoney());
    }

    private void showLoginAccount() {
        System.out.println("Your account information is as follows:");
        System.out.println("Card Id: " + loginAcc.getCardId());
        System.out.println("User Name: " + loginAcc.getUserName());
        System.out.println("Gender: " + loginAcc.getSex());
        System.out.println("Balance: " + loginAcc.getMoney());
        System.out.println("Single Cash Withdrawal Amount: " + loginAcc.getLimit());
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
