package mantra.codegen.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** Indicate field of OutputModelObject is an element to be walked by
 *  OutputModelWalker.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ModelElement {
}