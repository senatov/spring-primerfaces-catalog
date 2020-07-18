package de.senatov.reservatio.utl;



import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.model.ScheduleEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.sql.Timestamp;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;



@Configuration
@Data
@Slf4j
public class ScheduleRecordMapper {

	private static String SELECT_ALL_FROM_VIEW = "select s.schedule_id, u.id, u.e_mail, u.first_name, u.last_name, u.user_name, s.description, s.end_date, s.group_id, s.is_editable, s.schedule_id, s.start_date, s.style_class, s.title, s.url FROM schedule_db.sc_schedule s,   schedule_db.sc_user u where s.user_name_id = u.id ORDER BY s.schedule_id";
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private LinkedCaseInsensitiveMap map;
	private String title;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private String description;
	private String groupId;
	private String id;
	private Boolean isEditable;
	private String style;
	private String url;

	private List<Map<String, Object>> sheduleMaps;



	public void init() {

		sheduleMaps = jdbcTemplate.queryForList(SELECT_ALL_FROM_VIEW);
	}



	public void extractVal(Object sheduleMaps) throws Exception {

		map = (LinkedCaseInsensitiveMap) sheduleMaps;
		description = String.valueOf(map.get("description"));
		title = String.valueOf(map.get("title"));
		startDate = ((Timestamp) map.get("start_date")).toLocalDateTime();
		endDate = ((Timestamp) map.get("end_date")).toLocalDateTime();
		if (startDate.isAfter(endDate)) {
			String msg = String.format(" Wrong Event'%s': end Date before start Date! \nstartDate = %s\n startDate = %s", description, startDate, endDate);
			throw new DateTimeException(msg);
		}
		groupId = String.valueOf(map.get("group_id"));
		id = String.valueOf(map.get("id"));
		isEditable = (Boolean) map.get("is_editable");
		style = String.valueOf(map.get("style_class"));
		url = String.valueOf(map.get("url"));
	}



	public void saveEvent(ScheduleEvent event) {

		log.debug("saveEvent {}", event);
	}



	public void updateEvent(ScheduleEvent event) {

		log.debug("updateEvent {}", event);
	}

}
