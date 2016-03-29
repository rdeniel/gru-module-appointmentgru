
package fr.paris.lutece.plugins.appointmentgru.services;



import fr.paris.lutece.plugins.appointment.business.Appointment;

import fr.paris.lutece.plugins.appointmentgru.business.AppointmentGru;
import fr.paris.lutece.plugins.customerprovisioning.business.UserDTO;

import fr.paris.lutece.plugins.customerprovisioning.services.ProvisioningService;
import fr.paris.lutece.plugins.gru.business.customer.Customer;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import org.apache.commons.lang.StringUtils;

public  class AppointmentGruService {

	
	 public static final String BEAN_NAME = "appointmentgru.appointmentGruService";
	 /**
     * Instance of the service
     */
    private static volatile AppointmentGruService _instance;

    /** Singleton  AppointmentGruService
     * Get an instance of the service
     * @return An instance of the service
     */
    public static AppointmentGruService getService(  )
    {
        if ( _instance == null )
        {
            _instance = SpringContextService.getBean( BEAN_NAME );
        }

        return _instance;
    }
	
	public  AppointmentGru getAppointmentGru(Appointment appointment)
	{
	
		AppointmentGru appointmentGru = new AppointmentGru(appointment);	
                String strGuid = null;
                String strCuid = null;
                //hack for appointment when they make guid = admin admin
                   AppLogService.info("AppointmentGru DEBUT  : appointment.getIdUser() : "+appointment.getIdUser());
                if(StringUtils.isNumeric(appointment.getIdUser()))
                {
                strCuid = appointment.getIdUser();
                  AppLogService.info("AppointmentGru TEST  : strCuid OK "+appointment.getIdUser());
                }
                else 
                {
                 strGuid = appointment.getIdUser();
                    AppLogService.info("AppointmentGru TEST  : strGuid OK "+appointment.getIdUser()); 
                }
		
		 
                
                AppLogService.info("AppointmentGru  : GUID from appointment Guid: "+strGuid);
                AppLogService.info("AppointmentGru  : GUID from appointment Cuid: "+strCuid);
		
		Customer gruCustomer  = ProvisioningService.processGuidCuid( strGuid, strCuid, buildUserFromAppointment(appointment) );
		//call provisioning
		if(gruCustomer!=null)
		{
                    	AppLogService.info("\n\n\n------------------ AppointmentGru  -----------------------------");
		        AppLogService.info("AppointmentGru  : gruCustomer.getAccountGuid() : "+gruCustomer.getAccountGuid());
	         	AppLogService.info("AppointmentGru  : gruCustomer.getId() : "+gruCustomer.getId());
	
			appointmentGru.setGuid(gruCustomer.getAccountGuid());
			appointmentGru.setCuid(gruCustomer.getId()); 
			appointmentGru.setMobilePhoneNumber(gruCustomer.getMobilePhone()); 
		}		
	
		
		return appointmentGru;
	}
	
	  private  UserDTO buildUserFromAppointment( Appointment appointment )
	    {
	        UserDTO user = null;

	        if ( appointment != null )
	        {
	            user = new UserDTO(  );
	            user.setFirstname( appointment.getFirstName() ); 
	            user.setLastname( appointment.getLastName( ) );
	            user.setEmail( appointment.getEmail() );
	            user.setUid( appointment.getIdUser() );	       
	          
	        }

	        return user;
	    }
}
