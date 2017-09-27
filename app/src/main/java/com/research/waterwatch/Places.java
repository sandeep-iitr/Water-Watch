/*
This file is part of Water Watch.

    Water Watch is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Water Watch  is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Foobar.  If not, see <http://www.gnu.org/licenses/>.

*/

/*
Authors:
Sandeep Singh Sandha
UCLA, https://sites.google.com/view/sandeep-/home

Biplav Srivastava
IBM Research
 */

package com.research.waterwatch;

/**
 * Created by admin on 5/18/2015.
 */
public class Places {

    int Place_id;
    String Place_name;
    String Lat;
    String Lng;
    String Desc;

    String type;//maual or auto

    int data_type;// 0 or Null or not stored in server when only historical
    //1 when we have live sensor data
    //2 when we have both type of data for this place

    double range;

    Places()
    {
        Place_name="";
        Lat="";
        Lng="";
        Desc="";
        Place_id=-1;
        data_type=0;
        range=-1;
    }
}
