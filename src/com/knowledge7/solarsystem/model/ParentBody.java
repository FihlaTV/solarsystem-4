package com.knowledge7.solarsystem.model;

/***************************************************************************
 *   Copyright (C) 2005 by Avinash Meetoo - avinash@uom.ac.mu              *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU General Public License     *
 *   along with this program; if not, write to the                         *
 *   Free Software Foundation, Inc.,                                       *
 *   59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.             *
 ***************************************************************************/

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

/**
 * Natural bodies (like stars, planets and natural satellites) can have other
 * bodies orbiting around them (for instance, the moon orbits the earth). Those
 * "children" are kept in a linked list (meaning that there is no limitation on
 * the number of children) Normally, this class should have been abstract but we
 * want to unit-tests it.
 */

public class ParentBody extends Body
{
    /**
     * @param name is the name of the natural body
     * @param mass is the mass (in kg) of the natural body
     */
    public ParentBody(String name, double mass)
    {
        super(name, mass);

        // initially the list of orbiting bodies is empty
        orbitingBodies = new LinkedList<Body>();
    }

    /**
     * @return the description of this natural body which contains some details
     * concerning its (eventual) children
     */
    public String getDescription()
    {
        // the following simple code is used to generate descriptions such as
        // this one:
        // Earth ( Moon ( Luna ) Meteosat )
        // which can be read as:
        // The earth has two orbiting bodies, the Moon and Meteosat. The Moon
        // has
        // one orbiting body, Luna
        // Note that the code is NOT recursive but, rather, getDescription() is
        // being sent to each child in turn

        String description = getName() + "( ";

        for (Body orbitingBody : orbitingBodies) {
        	description += orbitingBody.getDescription() + " ";
        }

        description = description + ")";

        return description;
    }

    /**
     * @return the total mass (i.e. including the total masses of children)
     */
    public double getTotalMass()
    {
        double totalMass = getMass();

        for (Body body : orbitingBodies) {
            totalMass += body.getTotalMass();
        }

        return totalMass;
    }

    /**
     * This method is used to obtain a Java reference on an orbiting natural
     * body (either directly or indirectly) with a specific name
     * 
     * @param name is the name to fing
     * @return a reference on the natural body having this name, null if none found
     */
    public ParentBody getReferenceOnOrbitingNaturalBody(String name)
    {
        // are we looking for ourselves?
        if (getName().equals(name))
            return this;

        // no, so we just ask each child in turn
        for (Body body : orbitingBodies) {

            // We look only at natural bodies (or objects of derived classes)
            if (body instanceof ParentBody) {
                ParentBody orbitingNaturalBody = ((ParentBody) body).getReferenceOnOrbitingNaturalBody(name);

                if (orbitingNaturalBody != null) {
                    // We have found one which matches
                    return orbitingNaturalBody;
                }

                // Otherwise, we move to the next child
            }
        }

        // Here, we know that the name does not exist
        return null;
    }

    public boolean add(Body aBody)
    {
        String nameOfBodyToAdd = aBody.getName();

        // Do we already have something with the same name orbiting?
        for (Body orbitingBody : orbitingBodies) {
            if (orbitingBody.getName().equals(nameOfBodyToAdd))
                return false;
        }

        // No. So we can add the specified body to the list of bodies
        orbitingBodies.add(aBody);

        return true;
    }

    public boolean remove(String name)
    {
        Iterator<Body> i = orbitingBodies.iterator();

        while (i.hasNext()) {
            if (i.next().getName().equals(name)) {
                // We have found the body needed so we remove it and
                // stop our search because we know that a name cannot
                // appear twice (or more)
                i.remove();
                return true;
            }
        }

        // Here, we know that no body with that name exist
        return false;
    }

    /** this list contains all orbiting bodies around this natural body */
    private List<Body> orbitingBodies;
}