// FILE: UserCLIRunner.java
package com.example.demo.cli;

import com.example.demo.service.UserService;
import com.example.demo.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class UserCLIRunner implements CommandLineRunner {
    private final UserService userService;
    private final Scanner scanner = new Scanner(System.in);
    private boolean running = true;

    @Autowired
    public UserCLIRunner(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        System.out.println("Welcome to User Management System");
        System.out.println("Available commands:");
        printHelp();

        while (running) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            processCommand(input);
        }
    }

    private void processCommand(String input) {
        if (input.isEmpty()) {
            return;
        }

        String[] parts = input.split("\\s+");
        String command = parts[0].toLowerCase();

        try {
            switch (command) {
                case "add":
                    if (parts.length < 3) {
                        System.out.println("Usage: add <name> <surname>");
                    } else {
                        userService.addUser(parts[1], parts[2]);
                    }
                    break;
                case "get":
                    if (parts.length < 2) {
                        System.out.println("Usage: get <id>");
                    } else {
                        userService.getUser(Long.parseLong(parts[1]));
                    }
                    break;
                case "remove":
                    if (parts.length < 2) {
                        System.out.println("Usage: remove <id>");
                    } else {
                        userService.removeUser(Long.parseLong(parts[1]));
                    }
                    break;
                case "list":
                    if (userService instanceof UserServiceImpl) {
                        ((UserServiceImpl) userService).listAllUsers();
                    }
                    break;
                case "help":
                    printHelp();
                    break;
                case "exit":
                    System.out.println("Exiting application...");
                    running = false;
                    break;
                default:
                    System.out.println("Unknown command. Type 'help' for available commands.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: ID must be a number");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void printHelp() {
        System.out.println("  add <name> <surname> - Add a new user");
        System.out.println("  get <id> - Get user by ID");
        System.out.println("  remove <id> - Remove user by ID");
        System.out.println("  list - List all users");
        System.out.println("  help - Show this help");
        System.out.println("  exit - Exit the application");
    }
}