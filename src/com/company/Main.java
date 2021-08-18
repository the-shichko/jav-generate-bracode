package com.company;
import net.sf.jasperreports.engine.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Main {

    private final static String MARKING_FOLDER_PATH = "./Utils/Marking/";
    private final static String OUTPUT_FOLDER_PATH = MARKING_FOLDER_PATH + "Orders/Output/";
    private final static String INPUT_FOLDER_PATH = MARKING_FOLDER_PATH + "Orders/Input/jrxml";

    public static void main(String[] args) throws JRException {
        int exitCode = -1;
        Locale.setDefault(new Locale("ru", "RU"));
        System.out.println(Locale.getDefault().getCountry());
        System.out.println(args[1].getBytes(StandardCharsets.UTF_16)[0]);
        System.out.println(args[1]);
        System.out.println(args[2]);
        try {
            int i = 0;
            String orderGroup = args[0];
            String processGuid = args[1];
            String fileName = args[2];
            //for (String path: findFiles(Paths.get(INPUT_FOLDER_PATH + "/" + processGuid + "/"), "jrxml"))
            //{
            String path = Paths.get(INPUT_FOLDER_PATH + "/" + processGuid + "/").toString() + '/' + fileName + ".jrxml";
            JasperReport jasperReport = JasperCompileManager.compileReport(path);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), new JREmptyDataSource());
            JasperExportManager.exportReportToPdfFile(jasperPrint,
                    OUTPUT_FOLDER_PATH + processGuid + "/" + (orderGroup.isEmpty() ? "" : orderGroup + "/")
                     + String.format("\\%s.pdf", fileName));
            //}
            exitCode = 0;
        }
        catch (Exception e1) {
            System.out.println(e1.getMessage());
            exitCode = 1;
        }
        finally {
            System.out.println("Process exited with result code " + exitCode);
        }
    }

    public static List<String> findFiles(Path path, String fileExtension)
            throws IOException {

        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException("Path must be a directory!");
        }

        List<String> result;

        try (Stream<Path> walk = Files.walk(path)) {
            result = walk
                    .filter(p -> !Files.isDirectory(p))
                    .map(p -> p.toString().toLowerCase())
                    .filter(f -> f.endsWith(fileExtension))
                    .collect(Collectors.toList());
        }

        return result;
    }
}