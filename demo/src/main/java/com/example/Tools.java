package com.example;

import java.util.Random;

public class Tools {

    private static final String[] prefixes = {
        "Nova", "Peak", "Elite", "Prime", "Core", "Vital", "Next", "Swift",
        "Bright", "Clear", "Smart", "Echo", "Alpha", "Meta", "Golden", "Silver",
        "Apex", "Summit", "Fusion", "Synergy", "Insight", "Vision", "Horizon",
        "Everest", "Omega", "Zenith", "Quantum", "Prism", "Spectrum", "Venture"
    };

    private static final String[] suffixes = {
        "Tech", "Solutions", "Systems", "Dynamics", "Innovations", "Partners",
        "Enterprises", "Group", "Networks", "Logic", "Works", "Concepts",
        "Global", "Industries", "Ventures", "Labs", "Studio", "Collective",
        "Connect", "Forge", "Capital", "Experts", "Minds", "Consulting",
        "Services", "Wave", "Point", "Hub", "Nexus", "Matrix"
    };

    private static final String[] modifiers = {
        "Pro", "Plus", "Max", "Ultra", "Advanced", "Premium", "Superior",
        "Strategic", "Innovative", "Creative", "Digital", "Modern", "Global",
        "Integrated", "Unified", "Central", "Direct", "Dynamic", "Sustainable",
        "Reliable"
    };

    private static final String[] domains = {
        "Finance", "Health", "Data", "Cloud", "Mobile", "Web", "Cyber",
        "Media", "Energy", "Design", "Legal", "Supply", "Market", "Food",
        "Software", "Hardware", "Retail", "Medical", "Edu", "Bio", "Green",
        "Eco", "AI", "Smart", "Virtual", "Digital"
    };

    private static final String[] MALE_FIRST_NAMES = {
        "James", "John", "Robert", "Michael", "William", "David", "Richard", "Joseph",
        "Thomas", "Charles", "Christopher", "Daniel", "Matthew", "Anthony", "Mark",
        "Donald", "Steven", "Paul", "Andrew", "Joshua", "Kenneth", "Kevin", "Brian",
        "George", "Edward", "Ronald", "Timothy", "Jason", "Jeffrey", "Ryan", "Jacob",
        "Gary", "Nicholas", "Eric", "Stephen", "Jonathan", "Larry", "Justin", "Scott",
        "Brandon", "Benjamin", "Samuel", "Raymond", "Gregory", "Frank", "Alexander",
        "Patrick", "Jack", "Dennis", "Jerry", "Tyler"
    };

    private static final String[] FEMALE_FIRST_NAMES = {
        "Mary", "Patricia", "Jennifer", "Linda", "Barbara", "Elizabeth", "Susan",
        "Jessica", "Sarah", "Karen", "Nancy", "Lisa", "Betty", "Margaret", "Sandra",
        "Ashley", "Dorothy", "Kimberly", "Emily", "Donna", "Michelle", "Carol",
        "Amanda", "Melissa", "Deborah", "Stephanie", "Rebecca", "Laura", "Sharon",
        "Cynthia", "Kathleen", "Amy", "Shirley", "Angela", "Helen", "Anna", "Brenda",
        "Pamela", "Nicole", "Emma", "Samantha", "Katherine", "Christine", "Debra",
        "Rachel", "Catherine", "Carolyn", "Janet", "Ruth", "Maria", "Heather"
    };

    private static final String[] UNISEX_FIRST_NAMES = {
        "Jordan", "Taylor", "Morgan", "Casey", "Riley", "Avery", "Quinn", "Reese",
        "Charlie", "Skylar", "Sage", "River", "Dakota", "Phoenix", "Rowan", "Finley",
        "Hayden", "Peyton", "Cameron", "Logan", "Parker", "Drew", "Blake", "Emerson",
        "Sawyer", "Jesse", "Jamie Jamie", "Alexis", "Angel", "Kai"
    };

    private static final String[] LAST_NAMES = {
        "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis",
        "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson",
        "Thomas", "Taylor", "Moore", "Jackson", "Martin", "Lee", "Perez", "Thompson",
        "White", "Harris", "Sanchez", "Clark", "Ramirez", "Lewis", "Robinson", "Walker",
        "Young", "Allen", "King", "Wright", "Scott", "Torres", "Nguyen", "Hill", "Flores",
        "Green", "Adams", "Nelson", "Baker", "Hall", "Rivera", "Campbell", "Mitchell",
        "Carter", "Roberts", "Gomez", "Phillips", "Evans", "Turner", "Diaz", "Parker",
        "Cruz", "Edwards", "Collins", "Reyes", "Stewart", "Morris", "Morales", "Murphy",
        "Cook", "Rogers", "Gutierrez", "Ortiz", "Morgan", "Cooper", "Peterson", "Bailey",
        "Reed", "Kelly", "Howard", "Ramos", "Kim", "Cox", "Ward", "Richardson", "Watson",
        "Brooks", "Chavez", "Wood", "James", "Bennett", "Gray", "Mendoza", "Ruiz",
        "Hughes", "Price", "Alvarez", "Castillo", "Sanders", "Patel", "Myers", "Long",
        "Ross", "Foster", "Jimenez", "Powell", "Jenkins", "Perry", "Russell", "Sullivan",
        "Bell", "Coleman", "Butler", "Henderson", "Barnes", "Gonzales", "Fisher", "Vasquez",
        "Simmons", "Romero", "Jordan", "Patterson", "Alexander", "Hamilton", "Graham",
        "Reynolds", "Griffin", "Wallace", "Moreno", "West", "Cole", "Hayes", "Bryant",
        "Herrera", "Gibson", "Ellis", "Tran", "Medina", "Aguilar", "Stevens", "Murray",
        "Ford", "Castro", "Marshall", "Owens", "Harrison", "Fernandez", "McDonald", "Woods",
        "Washington", "Kennedy", "Wells", "Vargas", "Henry", "Chen", "Freeman", "Webb",
        "Tucker", "Guzman", "Burns", "Crawford", "Olson", "Simpson", "Porter", "Hunter",
        "Gordon", "Mendez", "Silva", "Shaw", "Snyder", "Mason", "Dixon", "Munoz", "Hunt",
        "Hicks", "Holmes", "Palmer", "Wagner", "Black", "Robertson", "Boyd", "Rose",
        "Stone", "Salazar", "Fox", "Warren", "Mills", "Meyer", "Rice", "Schmidt", "Garza",
        "Daniels", "Ferguson", "Nichols", "Stephens", "Soto", "Weaver", "Ryan", "Gardner",
        "Payne", "Grant", "Dunn", "Kelley", "Spencer", "Hawkins", "Arnold", "Pierce"
    };

    private static final String[] MIDDLE_NAMES = {
        "A.", "B.", "C.", "D.", "E.", "F.", "G.", "H.", "J.", "K.", "L.", "M.",
        "N.", "P.", "R.", "S.", "T.", "W.", "James", "Lee", "Michael", "Ann",
        "Marie", "Lynn", "Ray", "Joseph", "Robert", "Jean", "Mae", "Rose",
        "Grace", "Jane", "Elizabeth", "Thomas", "Alexander", "William", "Edward",
        "Louise", "Catherine", "Victoria"
    };

    private static final String[] PREFIXES = {"Dr.", "Mr.", "Ms.", "Mrs.", "Prof."};
    private static final String[] SUFFIXES = {"Jr.", "Sr.", "II", "III", "IV", "PhD", "MD", "Esq."};
    private Random random;

    Tools() {
        this.random = new Random();
    }

    public String GenerateName() {
        String pattern = "";

        switch (random.nextInt(4)) {
            case 1:
                pattern = String.format("%s %s", prefixes[random.nextInt(prefixes.length)], suffixes[random.nextInt(suffixes.length)]);
                break;
            case 2:
                pattern = String.format("%s %s", modifiers[random.nextInt(modifiers.length)], domains[random.nextInt(domains.length)]);
                break;
            case 3:
                pattern = String.format("%s %s", suffixes[random.nextInt(suffixes.length)], prefixes[random.nextInt(prefixes.length)]);
                break;
            case 4:
                pattern = String.format("%s %s", domains[random.nextInt(domains.length)], suffixes[random.nextInt(suffixes.length)]);
                break;
            default:
                break;
        }
        return pattern;
    }

    public String generateSimpleName() {
        String firstName = getRandomFirstName();
        String lastName = getRandomLastName();
        return firstName + " " + lastName;
    }

    private String getRandomFirstName() {
        int choice = random.nextInt(3);
        switch (choice) {
            case 0:
                return MALE_FIRST_NAMES[random.nextInt(MALE_FIRST_NAMES.length)];
            case 1:
                return FEMALE_FIRST_NAMES[random.nextInt(FEMALE_FIRST_NAMES.length)];
            default:
                return UNISEX_FIRST_NAMES[random.nextInt(UNISEX_FIRST_NAMES.length)];
        }
    }

    private String getRandomLastName() {
        return LAST_NAMES[random.nextInt(LAST_NAMES.length)];
    }

    public double LargeNumber_RandomGenerator() {
        double minExponent = -2;  // 10^-2 = 0.01
        double maxExponent = 70;  // 10^70
        // Generate a random exponent
        double randomExponent = minExponent + (Math.random() * (maxExponent - minExponent));
        // Return 10 raised to that exponent
        return Math.pow(10, randomExponent);
    }

    public double Random_InRange(double min, double max) {
        return min + (Math.random() * (max - min));
    }

    public static String Format_Number(Double number) {
        if (number < 1e6) {
            return String.format("%.2f", number);
        }
        String[] suffixes = {"Mill", "Bill", "Tril", "Quad", "Quin", "Sext", "Sept", "Octi", "Noni", "Deci", "Unde", "Duod", "Tred", "Quat", "Quin", "Sexd", "Sept", "Octo", "Nove", "Vigi", "Cent"};
        int magnitude = (int) Math.floor(Math.log10(number) / 3) - 2;
        if (magnitude >= suffixes.length) {
            magnitude = suffixes.length - 1;
        }
        double scaledValue = number / Math.pow(1000, magnitude + 2);
        return String.format("%.2f %s", scaledValue, suffixes[magnitude]);
    }
}
