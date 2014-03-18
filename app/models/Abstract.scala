// Copyright © 2014, German Neuroinformatics Node (G-Node)
//                   A. Stoewer (adrian.stoewer@rz.ifi.lmu.de)
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted under the terms of the BSD License. See
// LICENSE file in the root of the Project.

package models
import models.Model._
import java.util.{Set => JSet, TreeSet => JTreeSet}
import javax.persistence._



/**
 * A model class for abstracts
 */
@Entity
class Abstract extends Model with Owned {

  var title: String = _
  var topic: String = _
  var text:  String = _
  var doi:   String = _
  var conflictOfInterest: String = _
  var acknowledgements: String = _

  var sortId: Int = _

  @Convert(converter = classOf[AbstractStateConverter])
  var state: AbstractState.State = AbstractState.InPreparation

  @ManyToOne
  var conference : Conference = _
  @OneToMany(mappedBy = "abstr")
  var figures: JSet[Figure] = new JTreeSet[Figure]()

  @ManyToMany
  @JoinTable(name = "abstract_owners")
  var owners:  JSet[Account] = new JTreeSet[Account]()
  @OneToMany(mappedBy = "abstr", cascade = Array(CascadeType.ALL))
  var authors: JSet[Author] = new JTreeSet[Author]()
  @OneToMany(mappedBy = "abstr", cascade = Array(CascadeType.ALL))
  var affiliations: JSet[Affiliation] = new JTreeSet[Affiliation]()
  @OneToMany(mappedBy = "abstr", cascade = Array(CascadeType.ALL))
  var references: JSet[Reference] = new JTreeSet[Reference]()
}


object Abstract {

  def apply(uuid: Option[String],
            title: Option[String],
            topic: Option[String],
            text: Option[String],
            doi: Option[String],
            conflictOfInterest: Option[String],
            acknowledgements: Option[String],
            sortId: Option[Int],
            state: Option[AbstractState.State],
            conference: Option[Conference] = None,
            figures: Seq[Figure] = Nil,
            owners:  Seq[Account] = Nil,
            authors: Seq[Author] = Nil,
            affiliations: Seq[Affiliation] = Nil,
            references: Seq[Reference] = Nil) : Abstract = {

    val abstr = new Abstract()

    abstr.uuid        = unwrapRef(uuid)
    abstr.title       = unwrapRef(title)
    abstr.topic       = unwrapRef(topic)
    abstr.text        = unwrapRef(text)
    abstr.doi         = unwrapRef(doi)
    abstr.conflictOfInterest = unwrapRef(conflictOfInterest)
    abstr.acknowledgements   = unwrapRef(acknowledgements)
    abstr.sortId = sortId match { case Some(i) => i; case _ => 0 }
    abstr.state  = unwrapRef(state)

    abstr.conference  = unwrapRef(conference)
    abstr.figures      = toJSet(figures)
    abstr.owners      = toJSet(owners)
    abstr.authors     = toJSet(authors)
    abstr.affiliations = toJSet(affiliations)
    abstr.references  = toJSet(references)

    abstr
  }

}
