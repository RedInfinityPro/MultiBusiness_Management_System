package com.example;

import java.util.ArrayList;
import java.util.Random;

public class Tools {
    ArrayList<String> prefixes = new ArrayList<>();
    ArrayList<String> suffixes = new ArrayList<>();
    ArrayList<String> modifiers = new ArrayList<>();
    ArrayList<String> domains = new ArrayList<>();

    Tools() {
        buildNames();
    }

    private void buildNames() {
        String[] prefixes_list = {
            "Nova", "Peak", "Elite", "Prime", "Core", "Vital", "Next", "Swift",
            "Bright", "Clear", "Smart", "Echo", "Alpha", "Meta", "Golden", "Silver",
            "Apex", "Summit", "Fusion", "Synergy", "Insight", "Vision", "Horizon",
            "Everest", "Omega", "Zenith", "Quantum", "Prism", "Spectrum", "Venture"
        };

        String[] suffixes_list = {
            "Tech", "Solutions", "Systems", "Dynamics", "Innovations", "Partners",
            "Enterprises", "Group", "Networks", "Logic", "Works", "Concepts",
            "Global", "Industries", "Ventures", "Labs", "Studio", "Collective",
            "Connect", "Forge", "Capital", "Experts", "Minds", "Consulting",
            "Services", "Wave", "Point", "Hub", "Nexus", "Matrix"
        };

        String[] modifiers_list = {
            "Pro", "Plus", "Max", "Ultra", "Advanced", "Premium", "Superior",
            "Strategic", "Innovative", "Creative", "Digital", "Modern", "Global",
            "Integrated", "Unified", "Central", "Direct", "Dynamic", "Sustainable",
            "Reliable"
        };

        String[] domains_list = {
            "Finance", "Health", "Data", "Cloud", "Mobile", "Web", "Cyber",
            "Media", "Energy", "Design", "Legal", "Supply", "Market", "Food",
            "Software", "Hardware", "Retail", "Medical", "Edu", "Bio", "Green",
            "Eco", "AI", "Smart", "Virtual", "Digital"
        };

        for (String prefix : prefixes_list) {
            prefixes.add(prefix);
        }

        for (String suffix : suffixes_list) {
            suffixes.add(suffix);
        }

        for (String modifier : modifiers_list) {
            modifiers.add(modifier);
        }

        for (String domain : domains_list) {
            domains.add(domain);
        }
    }

    public String GenerateName() {
        String pattern = "";
        Random random = new Random();
        
        switch (random.nextInt(4)) {
            case 1:
                pattern = String.format("%s %s", prefixes.get(random.nextInt(prefixes.size())), suffixes.get(random.nextInt(suffixes.size())));
                break;
            case 2:
                pattern = String.format("%s %s", modifiers.get(random.nextInt(modifiers.size())), domains.get(random.nextInt(domains.size())));
                break;
            case 3:
                pattern = String.format("%s %s", suffixes.get(random.nextInt(suffixes.size())), prefixes.get(random.nextInt(prefixes.size())));
                break;
            case 4:
                pattern = String.format("%s %s", domains.get(random.nextInt(domains.size())), suffixes.get(random.nextInt(suffixes.size())));
                break;
            default:
                break;
        }
        return pattern;
    }

    public Integer LargeNumber_RandomGenerator() {
        double max = 100.00; //1000000000
        double min = 0.01;
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static String Format_Number(Double number) {
        if (number >= 1e6) {
            String[] suffixes = {"Mill", "Bill","Tril","Quad", "Quin", "Sext", "Sept", "Octi", "Noni", "Deci", "Unde", "Duod", "Tred", "Quat", "Quin", "Sexd", "Sept", "Octo", "Nove", "Vigi", "Cent"};
            int index = (int) Math.log10(number) / 3 - 2;
            double start = number / Math.pow(10, (6 + index * 3));
            String end = "";
            if (index < suffixes.length) {
                end = suffixes[index];
            }
            return String.format("%s %s", start, end);
        } else {
            return "" + number;
        } 
    }
}
