package com.thinkaurelius.titan.graphdb.query;

import com.thinkaurelius.titan.core.attribute.Cmp;
import com.thinkaurelius.titan.core.attribute.Contain;
import com.tinkerpop.gremlin.structure.Compare;
import com.tinkerpop.gremlin.structure.Contains;

import java.util.function.BiPredicate;

/**
 * Titan's extension of Blueprint's {@link Predicate} interface. Contains some custom methods that Titan needs for
 * query answering and evaluation.
 * </p>
 * This class contains a subclass used to convert Blueprint's {@link Predicate} to the corresponding Titan predicates.
 *
 * @author Matthias Broecheler (me@matthiasb.com)
 */
public interface TitanPredicate extends BiPredicate<Object, Object> {

    /**
     * Whether the given condition is a valid condition for this predicate.
     * </p>
     * For instance, the {@link Cmp#GREATER_THAN} would require that the condition is comparable and not null.
     *
     * @param condition
     * @return
     */
    public boolean isValidCondition(Object condition);

    /**
     * Whether the given class is a valid data type for a value to which this predicate may be applied.
     * </p>
     * For instance, the {@link Cmp#GREATER_THAN} can only be applied to {@link Comparable} values.
     *
     * @param clazz
     * @return
     */
    public boolean isValidValueType(Class<?> clazz);

    /**
     * Whether this predicate has a predicate that is semantically its negation.
     * For instance, {@link Cmp#EQUAL} and {@link Cmp#NOT_EQUAL} are negatives of each other.
     *
     * @return
     */
    public boolean hasNegation();

    /**
     * Returns the negation of this predicate if it exists, otherwise an exception is thrown. Check {@link #hasNegation()} first.
     * @return
     */
    public TitanPredicate negate();

    /**
     * Returns true if this predicate is in query normal form.
     * @return
     */
    public boolean isQNF();


    public boolean evaluate(Object value, Object condition);


    public static class Converter {

        /**
         * Convert Blueprint's comparison operators to Titan's
         *
         * @param p Any predicate
         * @return A TitanPredicate equivalent to the given predicate
         * @throws IllegalArgumentException if the given Predicate is unknown
         */
        public static final TitanPredicate convert(BiPredicate p) {
            if (p instanceof TitanPredicate) {
                return (TitanPredicate)p;
            } else if (p instanceof Compare) {
                Compare comp = (Compare)p;
                switch(comp) {
                    case eq: return Cmp.EQUAL;
                    case neq: return Cmp.NOT_EQUAL;
                    case gt: return Cmp.GREATER_THAN;
                    case gte: return Cmp.GREATER_THAN_EQUAL;
                    case lt: return Cmp.LESS_THAN;
                    case lte: return Cmp.LESS_THAN_EQUAL;
                    default: throw new IllegalArgumentException("Unexpected comparator: " + comp);
                }
            } else if (p instanceof Contains) {
                Contains con = (Contains)p;
                switch (con) {
                    case in: return Contain.IN;
                    case nin: return Contain.NOT_IN;
                    default: throw new IllegalArgumentException("Unexpected container: " + con);

                }
            } else throw new IllegalArgumentException("Titan does not support the given predicate: " + p);
        }

    }

}
