package com.kohls.cpe.darkside.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.xml.bind.annotation.*;


@XmlRootElement(name = "phoneNumber")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "PhoneNumber")
@Table(name = "phone_number")
public class PhoneNumber {

    @XmlAttribute
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(required = true)
    @Column(nullable = false)
    @NotEmpty
    private String code;

    @ApiModelProperty(required = true)
    @Column(nullable = false)
    @NotEmpty
    private String number;

    @ApiModelProperty(hidden = true)
    @JsonBackReference
    @ManyToOne
    @XmlTransient
    @JoinColumn(name = "contact_id")
    private Contact contact;
}
