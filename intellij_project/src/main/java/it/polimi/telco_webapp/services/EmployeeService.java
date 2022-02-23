package it.polimi.telco_webapp.services;

import it.polimi.telco_webapp.entities.Employee;
import it.polimi.telco_webapp.entities.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;

import java.security.InvalidParameterException;
import java.util.List;

@Stateless(name = "EmployeeService")
public class EmployeeService {
	@PersistenceContext(unitName = "telco_webapp")
	private EntityManager em;

	public EmployeeService() {
	}

	public Employee getEmployee(String employee_id) throws PersistenceException, InvalidParameterException {
		Employee employeeFromDB = em.find(Employee.class, employee_id);
		if (employeeFromDB == null)
			throw new InvalidParameterException("internal database error");
		else
			return employeeFromDB;
	}

	public Employee checkEmployeeCredentials(String employeeEmail, String password) {
		List<Employee> employees = em.createNamedQuery("Employee.checkCredentials", Employee.class).setParameter(1, employeeEmail).setParameter(2, password).getResultList();

		if (employees == null || employees.isEmpty()) {
			throw new InvalidParameterException("Invalid credentials.");
		} else if( employees.size() == 1) {
			return employees.get(0);
		} else {
			throw new InvalidParameterException("Internal database error: too many results.");
		}
	}
}
