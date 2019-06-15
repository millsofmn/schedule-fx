package miller.scheduler.service;

import miller.scheduler.service.dto.AppointmentDto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class ReportItem {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("M/yyyy")
            .withZone(ZoneId.systemDefault());
    private LocalDateTime startOfMoth;
    private LocalDateTime endOfMonth;
    private Map<String, List<AppointmentDto>> map = new HashMap<>();

    public ReportItem(AppointmentDto appointment){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(appointment.getStart(), ZoneId.systemDefault());
        startOfMoth = localDateTime.with(TemporalAdjusters.firstDayOfMonth());
        endOfMonth = localDateTime.with(TemporalAdjusters.lastDayOfMonth());

        String key = appointment.getDescription();
        if(!map.containsKey(appointment.getDescription())){
            map.put(key, new ArrayList<>());
        }
        map.get(key).add(appointment);
    }

    public void add(AppointmentDto appointment){
        String key = appointment.getDescription();

        if(!map.containsKey(appointment.getDescription())){
            map.put(key, new ArrayList<>());
        }
        map.get(key).add(appointment);
    }

    public boolean isDateIncluded(Instant instant){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        if(localDateTime.isAfter(startOfMoth) && localDateTime.isBefore(endOfMonth)){
            return true;
        }
        return false;
    }

    public LocalDateTime getStartOfMoth() {
        return startOfMoth;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(startOfMoth.format(DATE_FORMATTER)).append("\r\n");
        sb.append("-------------------------------------\r\n");
        for(String k : map.keySet()){
            sb.append("\t").append(k).append("\t\t").append(map.get(k).size()).append("\r\n");
        }
        sb.append("\r\n");
        return sb.toString();
    }

    public static Comparator<ReportItem> compareByDate() {
        return Comparator.nullsLast(Comparator.comparing(ReportItem::getStartOfMoth));
    }
}