import java.util.Locale;

class TestDrive {
    public static void main(String[] args) throws InterruptedException {
        Coffee simpleEspresso = new Espresso();
        System.out.println(/* write your code here */ + " $" + formatSum(/* write your code here */));

        Coffee espressoWithDecor = /* write your code here */;
        espressoWithDecor = new Milk(/* write your code here */);
        espressoWithDecor = new Sugar(/* write your code here */);
        System.out.println(/* write your code here */ + " $" + formatSum(/* write your code here */));

        Coffee instantWithDecor = /* write your code here */;
        instantWithDecor = new WhippedCream(/* write your code here */);
        instantWithDecor = new Sugar(/* write your code here */);
        instantWithDecor = new Sugar(/* write your code here */);
        instantWithDecor = new Sugar(/* write your code here */);
        System.out.println(/* write your code here */ + " $" + formatSum(/* write your code here */));

        System.out.println("I'm drinking my " + instantWithDecor.getDescription());
        Thread.sleep(1500);
        System.out.println("-I want to add some Whipped Cream to my coffee. And don't make a new one! Just add Whipped Cream");
        Thread.sleep(1500);
        System.out.println("-Okay! But the final price will change");
        Thread.sleep(1500);
        System.out.println("-I understand");

        instantWithDecor = new WhippedCream(/* write your code here */);
        System.out.println(/* write your code here */ + " $" + formatSum(/* write your code here */));
    }

    private static String formatSum(double sum) {
        return String.format(Locale.ROOT, "%.2f", sum);
    }
}

abstract class Coffee {
    String description;

    String getDescription() {
        return description;
    }

    abstract double cost();
}

class Espresso extends Coffee {

    Espresso() {
        description = "Espresso";
    }

    @Override
    double cost() {
        return 1.99;
    }
}

class InstantCoffee extends Coffee {

    InstantCoffee() {
        description = "Instant Coffee";
    }

    @Override
    double cost() {
        return 1.0;
    }
}

abstract class Decorator extends Coffee {
    abstract String getDescription();
}

class Milk extends Decorator {

    private Coffee coffee;

    /* write constructor */

    @Override
    String getDescription() {
        return /* write your code here */ + ", Milk";
    }

    @Override
    double cost() {
        return .13 + /* write your code here */;
    }
}

class Sugar extends Decorator {

    private Coffee coffee;

        /* write constructor */

    @Override
    String getDescription() {
        return /* write your code here */ + ", Sugar";
    }

    @Override
    double cost() {
        return .02 + /* write your code here */;
    }
}

class WhippedCream extends Decorator {

    private Coffee coffee;

        /* write constructor */

    @Override
    String getDescription() {
        return /* write your code here */ + ", WhippedCream";
    }

    @Override
    double cost() {
        return .10 + /* write your code here */;
    }
}