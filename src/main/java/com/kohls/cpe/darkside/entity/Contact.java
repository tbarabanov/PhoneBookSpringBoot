package com.kohls.cpe.darkside.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.xml.bind.annotation.*;
import java.util.HashSet;
import java.util.Set;

@XmlRootElement(name = "contact")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "contact")
public class Contact {

    @XmlAttribute
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(required = true)
    @Column(name = "first_name", nullable = false)
    @NotBlank
    private String firstName;

    @ApiModelProperty(required = true)
    @Column(name = "last_name", nullable = false)
    @NotBlank
    private String lastName;

    @XmlElementWrapper(name = "phoneNumbers")
    @XmlElement(name = "phoneNumber")
    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<PhoneNumber> phoneNumbers = new HashSet<>();

    public void addPhoneNumber(PhoneNumber phoneNumber) {
        phoneNumber.setContact(this);
        this.phoneNumbers.add(phoneNumber);
    }

    public void removePhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumbers.remove(phoneNumber);
        phoneNumber.setContact(null);
    }
}
