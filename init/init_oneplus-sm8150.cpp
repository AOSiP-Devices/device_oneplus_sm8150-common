/*
 * Copyright (C) 2019 The LineageOS Project
 * Copyright (C) 2020 The Potato Open Sauce Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include <android-base/logging.h>
#include <sys/sysinfo.h>
#include <android-base/properties.h>
#define _REALLY_INCLUDE_SYS__SYSTEM_PROPERTIES_H_
#include <sys/_system_properties.h>

#include "property_service.h"
#include "vendor_init.h"

using android::init::property_set;

void property_override(char const prop[], char const value[])
{
    prop_info *pi;

    pi = (prop_info*) __system_property_find(prop);
    if (pi)
        __system_property_update(pi, value, strlen(value));
    else
        __system_property_add(prop, strlen(prop), value, strlen(value));
}

void load_12gb()
{
    property_override("dalvik.vm.heapstartsize","24m");
    property_override("dalvik.vm.heapgrowthlimit","384m");
    property_override("dalvik.vm.heaptargetutilization","0.42");
    property_override("dalvik.vm.heapmaxfree","56m");
}

void load_8gb()
{
    property_override("dalvik.vm.heapstartsize","24m");
    property_override("dalvik.vm.heapgrowthlimit","256m");
    property_override("dalvik.vm.heaptargetutilization","0.46");
    property_override("dalvik.vm.heapmaxfree","48m");
}

void load_6gb()
{
    property_override("dalvik.vm.heapstartsize","16m");
    property_override("dalvik.vm.heapgrowthlimit","256m");
    property_override("dalvik.vm.heaptargetutilization","0.5");
    property_override("dalvik.vm.heapmaxfree","32m");
}

/* Get Ram size for different variants */
void checkram_loadprops()
{
    struct sysinfo sys;
    sysinfo(&sys);
    if (sys.totalram > 8192ull * 1024 * 1024) {
        load_12gb();
    }
    else if(sys.totalram > 6144ull * 1024 * 1024){
        load_8gb();
    }
    else if(sys.totalram > 4096ull * 1024 * 1024){
        load_6gb();
    }
}

void vendor_load_properties()
{
    checkram_loadprops();
}
