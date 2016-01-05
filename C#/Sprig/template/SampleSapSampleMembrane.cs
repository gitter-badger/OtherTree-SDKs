﻿#region copyright
/*
* This file is part of WhereIsMyTransport OtherTree (also referred to as simply OtherTree OR Wimt.OtherTree)
*
* OtherTree is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* Wimt.OtherTree is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Lesser General Public License for more details.
* You should have received a copy of the GNU Lesser General Public License
* along with Wimt.OtherTree.  If not, see <http://www.gnu.org/licenses/>
*
* OtherTree, Wimt.OtherTree and WhereIsMyTransport OtherTree remain the copyright (2015) of WhereIsMyTransport LTD <http://whereismytransport.com>
*/
#endregion
using System;
using Wimt.OtherTree;

namespace $safeprojectname$
{
    public class SampleSapSampleMembrane:Membrane<SampleSapSampleMembrane,SampleSap>
    {
        public override bool MayPassThrough(SampleSap sap)
        {
            //TODO Perform validation logic
            throw new NotImplementedException();
        }
    }
}