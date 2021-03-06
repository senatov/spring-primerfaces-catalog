package de.senatov.reservatio.view;



import de.senatov.reservatio.db.ScheduleEntity;
import de.senatov.reservatio.db.ScheduleService;
import de.senatov.reservatio.utl.ScheduleRecordMapper;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.time.LocalDateTime;

import static javax.faces.application.FacesMessage.SEVERITY_INFO;



@Configuration
@ManagedBean
@ViewScoped
@ToString
@Slf4j
public class ScheduleView implements Serializable {

	private static final String S_MINUTE_DELTA_S = "Day delta: %s,  Minute delta: %s";
	private static final long serialVersionUID = -2637195560425203881L;

	private final ScheduleModel eventModel = new DefaultScheduleModel();
	@Autowired
	ScheduleRecordMapper mapper;
	private ScheduleEvent event = new DefaultScheduleEvent();

	@Autowired
	private ScheduleService scheduleService;



	@PostConstruct
	public void init() throws Exception {

		mapper.init();
		for (Object value : mapper.getSheduleMaps()) {
			mapper.extractVal(value);
			eventModel.addEvent(DefaultScheduleEvent.builder()
			                                        .title(mapper.getTitle())
			                                        .startDate(mapper.getStartDate())
			                                        .endDate(mapper.getEndDate())
			                                        .description(mapper.getDescription())
			                                        .groupId(mapper.getGroupId())
			                                        .id(mapper.getId())
			                                        .editable(mapper.getIsEditable())
			                                        .styleClass(mapper.getStyle())
			                                        //.url(mapper.getUrl())  - Don't use it! This parameter already uised by PrimeFaces Schedule Controller's Event Editor.
			                                        .build());
		}
	}



	public ScheduleModel getEventModel() {

		log.debug("getEventModel() = {}", eventModel);
		return eventModel;
	}



	public ScheduleEvent getEvent() {

		log.debug("getEvent() = {}", event);
		return event;
	}



	public void setEvent(ScheduleEvent event) {

		log.debug("setEvent() = {}", event);
		this.event = event;
	}



	public void addEvent() {

		log.debug("addEvent() = {}", event);
		if (event.getId() == null) {
			eventModel.addEvent(event);
			ScheduleEntity schedule = mapper.mapEvent(event);
			scheduleService.updateSchedule(schedule);
		}
		else {
			eventModel.updateEvent(event);
			ScheduleEntity schedule = mapper.mapEvent(event);
			scheduleService.updateSchedule(schedule);
		}
		event = new DefaultScheduleEvent();
	}



	public void onEventSelect(SelectEvent selectEvent) {

		log.debug("onEventSelect() = {}", selectEvent);
		event = (ScheduleEvent) selectEvent.getObject();
	}



	public void onDateSelect(SelectEvent selectEvent) {

		event = DefaultScheduleEvent.builder()
		                            .title("")
		                            .startDate((LocalDateTime) selectEvent.getObject())
		                            .endDate((LocalDateTime) selectEvent.getObject())
		                            .build();
	}



	public void onEventMove(ScheduleEntryMoveEvent event) {

		String strMsg = String.format(S_MINUTE_DELTA_S, event.getDayDelta(), event.getMinuteDelta());
		FacesMessage message = new FacesMessage(SEVERITY_INFO, "Event moved", strMsg);
		addMessage(message);
	}



	public void onEventResize(ScheduleEntryResizeEvent event) {

		String strMsg = String.format(S_MINUTE_DELTA_S, event.getDayDeltaEnd(), event.getMinuteDeltaEnd());
		FacesMessage message = new FacesMessage(SEVERITY_INFO, "Event resized", strMsg);
		addMessage(message);
	}



	private void addMessage(FacesMessage message) {

		FacesContext.getCurrentInstance()
		            .addMessage(null, message);
	}

}

