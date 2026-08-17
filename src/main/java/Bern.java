public class Bern {
    private static final String messageLine = "____________________________________\n";
    private static final String chatbotBanner = " ____                  \n"
            + "|  _ \\                 \n"
            + "| |_) | ___ _ __ _ __  \n"
            + "|  _ < / _ \\ '__| '_ \\ \n"
            + "| |_) |  __/ |  | | | |\n"
            + "|____/ \\___|_|  |_| |_|";
    private static final String chatbotName = "Bern Tokens";
    private static final String greetingTemplate = "%s\n"
            + messageLine
            + "Hello! I'm %s.\n"
            + "What can I do for you?\n";

    private static String getGreetingMessage() {
        return messageLine + String.format(greetingTemplate, chatbotBanner ,chatbotName) + messageLine;
    }

    private static String getExitMessage() {
        return "Bye. Hope to see you again soon!\n" + messageLine;
    }

    public static void main(String[] args) {
        System.out.print(getGreetingMessage());
        System.out.print(getExitMessage());
    }
}
