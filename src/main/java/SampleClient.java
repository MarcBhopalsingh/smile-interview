import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.bundle.BundleEntryParts;
import org.apache.commons.lang3.ObjectUtils;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;

import java.util.*;


public class SampleClient {

    public static void main(String[] theArgs) {

        // Create a FHIR client
        FhirContext fhirContext = FhirContext.forR4();
        IGenericClient client = fhirContext.newRestfulGenericClient("http://hapi.fhir.org/baseR4");
        client.registerInterceptor(new LoggingInterceptor(false));

        // Search for Patient resources
        Bundle response = client
                .search()
                .forResource("Patient")
                .where(Patient.FAMILY.matches().value("SMITH"))
                .returnBundle(Bundle.class)
                .execute();

        System.out.println("Found " + response.getEntry().size() + " patients named 'Smith'");

        List<BundleEntryParts> resultsList = BundleUtil.toListOfEntries(fhirContext, response);
        ArrayList<Patient> patients = new ArrayList();

        for (BundleEntryParts url : resultsList) {
            Patient p = client.read()
                    .resource(Patient.class)
                    .withUrl(url.getFullUrl())
                    .execute();
            patients.add(p);
        }

        Collections.sort(patients, new PatientComparator());

        for (Patient p : patients) {
            List<HumanName> names = p.getName();
                System.out.print(names.get(0).getGivenAsSingleString() + " ");
                if (p.getBirthDate() != null) {
                    System.out.println(names.get(0).getFamily() + " " + p.getBirthDate().toString());

                } else {
                    System.out.println(names.get(0).getFamily());

                }
        }
    }
}
