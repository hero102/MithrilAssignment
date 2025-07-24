package com.aurionpro.service;


import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import com.aurionpro.model.Builder;
import com.aurionpro.model.Guitar;
import com.aurionpro.model.InputValidator;
import com.aurionpro.model.Type;
import com.aurionpro.model.Wood;

public class GuitarFilterService {
    private final Scanner sc;

    public GuitarFilterService(Scanner sc) {
        this.sc = sc;
    }

    public List<Guitar> applyDynamicFilter(List<Guitar> list) {
        System.out.println("Filter by:");
        System.out.println("1. Model\n2. Type\n3. Builder\n4. Back Wood\n5. Top Wood");

        int filterChoice = InputValidator.getValidatedInt(sc, "Select filter: ", 1, 5);

        List<Guitar> filteredList = list;

        switch (filterChoice) {
            case 1:
                List<String> models = list.stream()
                        .map(g -> g.getSpec().getModel())
                        .distinct()
                        .sorted()
                        .toList();

                if (models.isEmpty()) return Collections.emptyList();

                int modelChoice = GuitarDisplayUtil.printMenuAndSelect(sc, models, "Select Model");
                filteredList = list.stream()
                        .filter(g -> g.getSpec().getModel().equalsIgnoreCase(models.get(modelChoice)))
                        .toList();
                break;

            case 2:
                List<Type> types = list.stream()
                        .map(g -> g.getSpec().getType())
                        .distinct()
                        .toList();

                if (types.isEmpty()) return Collections.emptyList();

                int typeChoice = GuitarDisplayUtil.printMenuAndSelect(sc, types, "Select Type");
                filteredList = list.stream()
                        .filter(g -> g.getSpec().getType() == types.get(typeChoice))
                        .toList();
                break;

            case 3:
                List<Builder> builders = list.stream()
                        .map(g -> g.getSpec().getBuilder())
                        .distinct()
                        .toList();

                if (builders.isEmpty()) return Collections.emptyList();

                int builderChoice = GuitarDisplayUtil.printMenuAndSelect(sc, builders, "Select Builder");
                filteredList = list.stream()
                        .filter(g -> g.getSpec().getBuilder() == builders.get(builderChoice))
                        .toList();
                break;

            case 4:
                List<Wood> backWoods = list.stream()
                        .map(g -> g.getSpec().getBackWood())
                        .distinct()
                        .toList();

                if (backWoods.isEmpty()) return Collections.emptyList();

                int backChoice = GuitarDisplayUtil.printMenuAndSelect(sc, backWoods, "Select Back Wood");
                filteredList = list.stream()
                        .filter(g -> g.getSpec().getBackWood() == backWoods.get(backChoice))
                        .toList();
                break;

            case 5:
                List<Wood> topWoods = list.stream()
                        .map(g -> g.getSpec().getTopWood())
                        .distinct()
                        .toList();

                if (topWoods.isEmpty()) return Collections.emptyList();

                int topChoice = GuitarDisplayUtil.printMenuAndSelect(sc, topWoods, "Select Top Wood");
                filteredList = list.stream()
                        .filter(g -> g.getSpec().getTopWood() == topWoods.get(topChoice))
                        .toList();
                break;

            default:
                System.out.println("Invalid filter option.");
                break;
        }

        return filteredList;
    }
}
