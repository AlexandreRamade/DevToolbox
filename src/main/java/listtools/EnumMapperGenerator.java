package listtools;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import filesmanager.FilesReader;

/**
 * EnumMapperGenerator
 * <p>
 * Génère un mapper Java qui mappe les valeurs d'une enum en une autre enum
 * </p>
 * <u>Précaution : les valeurs corespondantes doivent être dans le même ordre dans les 2 listes</u>
 */
public class EnumMapperGenerator {
	
	private String enumSourceName;
	private String varibleName;
	private List<String> enumSourceValues;
	
	private String enumResultName;
	private List<String> enumResultValues;
	
	private boolean privateMethod = false;
	private boolean staticMethod = false;
	private boolean nullCheck = false;
	private String nullCheckReturnValue = "null";
	private String switchCaseDefaultValue = "null";
	
	private static final String SEPARATOR = "\r\n";
	private static final String ENUM_VALUES_SEPARATOR = ",";
	private static final String ENUM_VALUE_PATTERN = "^[A-Z_]+";
	private static final String ENUM_NAME_EXTRACTOR_PATTERN = "enum ([A-Z][A-Za-z0-9]+) ";
	
	/** ***** ***** CONSTRUCTORS ***** ***** */
	
	public EnumMapperGenerator(String enumSourceName, List<String> enumSourceValues, String enumResultName,
			List<String> enumResultValues) {
		this.enumSourceName = enumSourceName;
		// par défaut, le libelle de la variable correspond au nom de l'enum source en camelCase
		this.varibleName = enumSourceName.substring(0, 1).toLowerCase() + enumSourceName.substring(1);
		this.enumSourceValues = enumSourceValues;
		this.enumResultName = enumResultName;
		this.enumResultValues = enumResultValues;
	}
	
	public EnumMapperGenerator(String enumSourceName, String enumSourceValues, String enumResultName,
			String enumResultValues, String separator) {
		this(enumSourceName, extractEnumValuesFromString(enumSourceValues, separator), enumResultName, extractEnumValuesFromString(enumResultValues, separator));
	}
	
	private static List<String> extractEnumValuesFromString(String source, String separator) {		
		return new StringListManager(source, separator != null ? separator : SEPARATOR)
				.decouperChaqueElement(ENUM_VALUES_SEPARATOR)
				.supprimerEspaces()
				.extraireMotif(ENUM_VALUE_PATTERN)
				.getListe();
	}
		
	public static EnumMapperGenerator newEnumMapperGeneratorFromFiles(String enumSourceFilePath, String enumSourceFileName, String enumResultFilePath, String enumResultFileName) {
		List<String> enumSourceFileLines = FilesReader.readAllLinesInFile(enumSourceFilePath, enumSourceFileName);
		List<String> enumResultFileLines = FilesReader.readAllLinesInFile(enumResultFilePath, enumResultFileName);
		return new EnumMapperGenerator(
				extractEnumNameFromFile(enumSourceFileLines), 
				extractEnumValuesFromFile(enumSourceFileLines),
				extractEnumNameFromFile(enumResultFileLines), 
				extractEnumValuesFromFile(enumResultFileLines));
	}
	
	private static String extractEnumNameFromFile(List<String> fileLines) {
		return new StringListManager(fileLines).extraireVariableUneSeuleFoisParLigne(ENUM_NAME_EXTRACTOR_PATTERN).getFirstValue().get();
	}
	
	private static List<String> extractEnumValuesFromFile(List<String> fileLines) {
		return new StringListManager(fileLines).decouperChaqueElement(ENUM_VALUES_SEPARATOR).supprimerEspaces().extraireMotif(ENUM_VALUE_PATTERN).getListe();
	}

	/** ***** ***** AFFICHAGE DU RESULTAT ***** ***** */
	
	public void generateAndDisplayMapper() {
		System.out.println(generateMapper());
	}
	
	/** ***** ***** METHODES DE TRAITEMENT ***** ***** */
	
	private String generateMapper() {
		StringBuilder mapper = new StringBuilder("");
		// 1st line
		mapper.append(privateMethod ? "private" : "public").append(" ");
		mapper.append(staticMethod ? "static " : "");
		mapper.append(enumResultName).append(" ");
		mapper.append("get").append(enumResultName).append("(").append(enumSourceName).append(" ").append(varibleName).append(") {").append("\n");
		
		// null check
		if(nullCheck) {
			mapper.append("\t").append("if(").append(varibleName).append(" == null) {").append("\n");
			mapper.append("\t\t").append("return ").append(nullCheckReturnValue).append(";").append("\n");
			mapper.append("\t").append("}").append("\n");
		}
		
		//switch case
		mapper.append("\t").append("switch (").append(varibleName).append(") {").append("\n");
		for(int i = 0; i < enumSourceValues.size() && i < enumResultValues.size(); i++) {
			mapper.append("\t\t").append("case ").append(enumSourceValues.get(i)).append(":").append("\n");
			mapper.append("\t\t\t").append("return ").append(enumResultName).append(".").append(enumResultValues.get(i)).append(";").append("\n");
		}
		mapper.append("\t\t").append("default:").append("\n");
		mapper.append("\t\t\t").append("return ").append(switchCaseDefaultValue).append(";").append("\n");
		mapper.append("\t").append("}").append("\n");
		
		// method end
		mapper.append("}");
		
		return mapper.toString();
	}
	
	/** ***** ***** METHODES DE PARAMETRAGE ***** ***** */

	public EnumMapperGenerator privateMethod() {
		this.privateMethod = true;
		return this;
	}

	public EnumMapperGenerator staticMethod() {
		this.staticMethod = true;
		return this;
	}
	
	public EnumMapperGenerator variableName(String variableName) {
		this.varibleName = variableName;
		return this;
	}
	
	public EnumMapperGenerator nullCheck() {
		this.nullCheck = true;
		return this;
	}

	public EnumMapperGenerator nullCheck(String returnValueIfNull) {
		this.nullCheck = true;
		this.nullCheckReturnValue = returnValueIfNull;
		return this;
	}

	public EnumMapperGenerator setDefaultValue(String switchCaseDefaultValue) {
		this.switchCaseDefaultValue = switchCaseDefaultValue;
		return this;
	}
	
	

}
