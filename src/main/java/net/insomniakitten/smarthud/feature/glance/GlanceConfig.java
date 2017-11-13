package net.insomniakitten.smarthud.feature.glance;
 
/*
 *  Copyright 2017 InsomniaKitten
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

import net.minecraftforge.common.config.Config;

public final class GlanceConfig {

    @Config.Name("Is Enabled")
    @Config.Comment({ "Should the HUD be enabled? If false, the HUD won't render." })
    @Config.LangKey("config.smarthud.glance.enabled")
    public boolean isEnabled = true;

    @Config.Name("Require Sneaking")
    @Config.Comment({ "Should the HUD only render when sneaking?" })
    @Config.LangKey("config.smarthud.glance.require_sneaking")
    public boolean requireSneaking = true;

    @Config.Name("Require Probe")
    @Config.Comment({ "Should the HUD only render when the player has a probe equipped?",
                      "This depends on mod \"The One Probe\" being present." })
    @Config.LangKey("config.smarthud.glance.require_probe")
    public boolean requireProbe = false;

}
