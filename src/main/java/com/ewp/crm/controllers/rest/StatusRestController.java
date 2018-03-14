package com.ewp.crm.controllers.rest;

import com.ewp.crm.models.Client;
import com.ewp.crm.models.ClientHistory;
import com.ewp.crm.models.Status;
import com.ewp.crm.models.User;
import com.ewp.crm.service.interfaces.ClientService;
import com.ewp.crm.service.interfaces.StatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/rest/status")
public class StatusRestController {

	private static Logger logger = LoggerFactory.getLogger(StatusRestController.class);

	private final StatusService statusService;
	private final ClientService clientService;

	@Autowired
	public StatusRestController(StatusService statusService, ClientService clientService) {
		this.statusService = statusService;
		this.clientService = clientService;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity addNewStatus(@RequestParam(name = "statusName") String statusName) {
		statusService.add(new Status(statusName));
		User currentAdmin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		logger.info("{} has added status with name: {}", currentAdmin.getFullName(), statusName);
		return ResponseEntity.ok("Успешно добавлено");
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ResponseEntity editStatus(@RequestParam(name = "statusName") String statusName, @RequestParam(name = "oldStatusId") Long oldStatusId) {
		Status status = statusService.get(oldStatusId);
		status.setName(statusName);
		statusService.update(status);
		User currentAdmin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		logger.info("{} has updated status {}", currentAdmin.getFullName(), statusName);
		return ResponseEntity.ok().build();
	}

	@RequestMapping(value = "/change", method = RequestMethod.POST)
	public ResponseEntity changeClientStatus(@RequestParam(name = "statusId") Long statusId,
	                                         @RequestParam(name = "clientId") Long clientId) {
		Client currentClient = clientService.getClientByID(clientId);
		if (currentClient.getStatus().getId().equals(statusId)) {
			return ResponseEntity.badRequest().build();
		}
		User currentAdmin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		currentClient.addHistory(new ClientHistory(currentAdmin.getFullName() + " изменил статус c " + currentClient.getStatus().getName() + " на " + statusService.get(statusId).getName()));
		statusService.changeClientStatus(clientId, statusId);
		logger.info("{} has changed status of client with id: {} to status id: {}", currentAdmin.getFullName(), clientId, statusId);
		return ResponseEntity.ok().build();
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity deleteStatus(@RequestParam(name = "deleteId") Long deleteId) {
		if(deleteId==1L){
			return ResponseEntity.badRequest().build();
		}
		statusService.delete(deleteId);
		User currentAdmin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		logger.info("{} has  deleted status  with id {}", currentAdmin.getFullName(), deleteId);
		return ResponseEntity.ok().build();
	}


}
