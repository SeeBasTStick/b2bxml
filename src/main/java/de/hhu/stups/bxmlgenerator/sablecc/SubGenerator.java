package de.hhu.stups.bxmlgenerator.sablecc;

import java.util.List;

public interface SubGenerator {

    String generateAllExpression();

    List<String> generateSubExpression();
}
