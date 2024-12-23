package horizontal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

/**
 * Represents a client in the system.
 */
@Data
@Builder(setterPrefix = "set")
public class Client {
    @JsonIgnore
    private String _name;

    @Builder.Default
    private UUID _id = UUID.randomUUID();
    @Builder.Default
    private String _passport = "";
    @Builder.Default
    private String _address = "";

    /**
     * Creates a new Client instance with the specified name.
     *
     * @param name The name of the client.
     * @return A new ClientBuilder instance.
     */
    public static ClientBuilder builder(String name) {
        return new ClientBuilder().set_name(name);
    }

    /**
     * Checks if the client is verified.
     * A client is considered verified if both passport and address are not empty.
     *
     * @return true if the client is verified, false otherwise.
     */
    public boolean isVerified() {
        return !_passport.isEmpty() &&
                !_address.isEmpty();
    }

    /**
     * Prints the client details.
     * Prints the client's ID, name, and verification status.
     */
    public void print() {
        System.out.println("ID: " + _id);
        System.out.println("Name : " + _name);
        System.out.println(isVerified() ? "Verified" : "Unverified");
    }
}