package com.tuon.logs.reports;

import com.tuon.logs.audit.AuditAction;
import com.tuon.logs.audit.AuditLog;
import com.tuon.logs.audit.AuditLogService;
import com.tuon.logs.audit.EntityType;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AuditReportService {

    private final String strPath = "C:\\Users\\anasc\\OneDrive\\Desktop\\Tuon\\Portfólio\\Academia - Java\\GymSystem\\reports\\audit";
    private final AuditLogService service = new AuditLogService();

    private void createFolder(String strPath) {
        File folder = new File(strPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    public void exportAllLogs() {
        createFolder(strPath);
        List<AuditLog> logs = service.findAll();
        String filePath = strPath + "\\all\\all_logs" + "_" + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm")) + ".csv";
        writeCsv(filePath, logs);
        // Lógica para exportar todos os logs para o arquivo CSV
    }

    public void exportLogsByEmployee(Integer employeeId) {
        createFolder(strPath);
        List<AuditLog> logs = service.findByEmployee(employeeId);
        String filePath = strPath + "\\by_employee\\logs_employee_" + employeeId + "_" + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm")) + ".csv";
        writeCsv(filePath, logs);
        // Lógica para exportar os logs do funcionário específico para o arquivo CSV
    }

    public void exportLogsByAction(AuditAction action) {
        createFolder(strPath);
        List<AuditLog> logs = service.findByAction(action);
        String filePath = strPath + "\\by_action\\logs_action_" + action + "_" + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm")) + ".csv";
        writeCsv(filePath, logs);
        // Lógica para exportar os logs de uma ação específica para o arquivo CSV
    }

    public void exportLogsByEntityType(EntityType entityType) {
        createFolder(strPath);
        List<AuditLog> logs = service.findByEntityType(entityType);
        String filePath = strPath + "\\by_entity_type\\logs_entity_type_" + entityType + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm")) + ".csv";
        writeCsv(filePath, logs);
        // Lógica para exportar os logs de um tipo de entidade específico para o arquivo CSV
    }

    public void exportLogsByDateRange(LocalDateTime start, LocalDateTime end) {
        createFolder(strPath);
        List<AuditLog> logs = service.findByDateRange(start, end);
        String filePath = strPath + "\\by_date\\logs_date_range_" + start.format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm")) + "_to_" + end.format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm")) + "_" + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm")) + ".csv";
        writeCsv(filePath, logs);
        // Lógica para exportar os logs dentro de um intervalo de datas específico para o arquivo CSV
    }

    private void writeCsv(String filePath, List<AuditLog> logs) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            // Cabeçalho
            bw.write("id;employee_id;action;entity_type;entity_id;description;success;created_at");
            bw.newLine();

            // Dados
            for (AuditLog log : logs) {
                bw.write(
                        log.getId() + ";" +
                                log.getEmployeeId() + ";" +
                                log.getAction() + ";" +
                                log.getEntityType() + ";" +
                                log.getEntityId() + ";" +
                                log.getDescription() + ";" +
                                log.getSuccess() + ";" +
                                log.getCreatedAt()
                );
                bw.newLine();
            }
            System.out.println("Report exported successfully: " + filePath);

        } catch (IOException e) {
            throw new RuntimeException("Error exporting report: " + e.getMessage());
        }
    }


}
