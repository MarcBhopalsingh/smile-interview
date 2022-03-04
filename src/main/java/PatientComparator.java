import org.hl7.fhir.r4.model.Patient;

import java.util.Comparator;

public class PatientComparator implements Comparator<Patient> {
    @Override
    public int compare(Patient p1, Patient p2) {
        String name1 = p1.getName().get(0).getGivenAsSingleString().toLowerCase();
        String name2 = p2.getName().get(0).getGivenAsSingleString().toLowerCase();
        return name1.compareTo(name2);
    }
}
