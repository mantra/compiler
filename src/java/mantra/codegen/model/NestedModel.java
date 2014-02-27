package mantra.codegen.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** Indicate field of OutputModelObject is an element to be walked by
 *  OutputModelWalker. Do not use on regular model fields like name and size.
 *  Those are accessed via modelobject.name etc...
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface NestedModel {
}