import java.util.List;

/**
 * Abstracts add-on pricing. New add-ons can be added by extending this logic.
 */
public interface AddOnPricer {
    Money totalFor(List<AddOn> addOns);
}
