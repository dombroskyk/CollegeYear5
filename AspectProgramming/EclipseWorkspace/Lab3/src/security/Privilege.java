package security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a method is restricted in who may call it.
 * It is assumed that the first argument to any method so annotated
 * has a first parameter that is an instance of Ticket.
 * @author James Heliotis
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.METHOD )
public @interface Privilege {
    /**
     * A method with this annotation must have a first parameter
     * of type Ticket.Level whose value equals or exceeds that of
     * this annotation instance's value.
     */
    Level value();
}
