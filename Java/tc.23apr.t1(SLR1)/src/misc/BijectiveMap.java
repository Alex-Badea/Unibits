package misc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// TODO: încă neimplement(at|abil)
/**
 *  Indică faptul că valorile unui Map adnotat cu @BijectiveMap se poate transforma în Set fără pierdere de informații
 *  și faptul că Map-ul respectiv este inversabil
 */

@Documented
@Target({ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.PARAMETER, ElementType.METHOD})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface BijectiveMap {
}
