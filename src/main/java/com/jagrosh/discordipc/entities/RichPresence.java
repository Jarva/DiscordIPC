/*
 * Copyright 2017 John Grosh (john.a.grosh@gmail.com).
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
package com.jagrosh.discordipc.entities;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.helpers.FormattingTuple;

/**
 * An encapsulation of all data needed to properly construct a JSON RichPresence payload.
 *
 * <p>These can be built using {@link RichPresence.Builder}.
 *
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class RichPresence
{
    private final String state;
    private final String details;
    private final OffsetDateTime startTimestamp;
    private final OffsetDateTime endTimestamp;
    private final String largeImageKey;
    private final String largeImageText;
    private final String smallImageKey;
    private final String smallImageText;
    private final String partyId;
    private final int partySize;
    private final int partyMax;
    private final String matchSecret;
    private final String joinSecret;
    private final String spectateSecret;
    private final boolean instance;
    private final HashMap<String, String> buttons;
    
    public RichPresence(String state, String details, OffsetDateTime startTimestamp, OffsetDateTime endTimestamp, 
            String largeImageKey, String largeImageText, String smallImageKey, String smallImageText, 
            String partyId, int partySize, int partyMax, String matchSecret, String joinSecret, 
            String spectateSecret, boolean instance, HashMap<String, String> buttons)
    {
        this.state = state;
        this.details = details;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.largeImageKey = largeImageKey;
        this.largeImageText = largeImageText;
        this.smallImageKey = smallImageKey;
        this.smallImageText = smallImageText;
        this.partyId = partyId;
        this.partySize = partySize;
        this.partyMax = partyMax;
        this.matchSecret = matchSecret;
        this.joinSecret = joinSecret;
        this.spectateSecret = spectateSecret;
        this.instance = instance;
        this.buttons = buttons;
    }



    /**
     * Constructs a {@link JSONObject} representing a payload to send to discord
     * to update a user's Rich Presence.
     *
     * <p>This is purely internal, and should not ever need to be called outside of
     * the library.
     *
     * @return A JSONObject payload for updating a user's Rich Presence.
     */
    public JSONObject toJson()
    {
        JSONObject jo = new JSONObject();
        jo.put("state", state);
        jo.put("details", details);
        if (startTimestamp != null || endTimestamp != null) {
            jo.put("timestamps", new JSONObject()
                    .put("start", startTimestamp == null ? null : startTimestamp.toEpochSecond())
                    .put("end", endTimestamp == null ? null : endTimestamp.toEpochSecond())
            );
        }
        if (startTimestamp != null || endTimestamp != null) {
            jo.put("timestamps", new JSONObject()
                    .put("start", startTimestamp == null ? null : startTimestamp.toEpochSecond())
                    .put("end", endTimestamp == null ? null : endTimestamp.toEpochSecond())
            );
        }
        if (largeImageKey != null || largeImageText != null || smallImageKey != null || smallImageText != null) {
            jo.put("assets", new JSONObject()
                    .put("large_image", largeImageKey)
                    .put("large_text", largeImageText)
                    .put("small_image", smallImageKey)
                    .put("small_text", smallImageText)
            );
        }
        if (partyId != null) {
            jo.put("party", new JSONObject()
                    .put("id", partyId)
                    .put("size", new JSONArray().put(partySize).put(partyMax))
            );
        }
        if (joinSecret != null || spectateSecret != null || matchSecret != null) {
            jo.put("secrets", new JSONObject()
                    .put("join", joinSecret)
                    .put("spectate", spectateSecret)
                    .put("match", matchSecret)
            );
        }
        if (buttons.size() > 0) {
            jo.put("buttons", new JSONArray(buttons));
        }
        jo.put("instance", instance);

        return jo;
    }

    /**
     * A chain builder for a {@link RichPresence} object.
     *
     * <p>An accurate description of each field and it's functions can be found
     * <a href="https://discordapp.com/developers/docs/rich-presence/how-to#updating-presence-update-presence-payload-fields">here</a>
     */
    public static class Builder
    {
        private String state;
        private String details;
        private OffsetDateTime startTimestamp;
        private OffsetDateTime endTimestamp;
        private String largeImageKey;
        private String largeImageText;
        private String smallImageKey;
        private String smallImageText;
        private String partyId;
        private int partySize;
        private int partyMax;
        private String matchSecret;
        private String joinSecret;
        private String spectateSecret;
        private boolean instance;
        private final HashMap<String, String> buttons = new HashMap<>();

        /**
         * Builds the {@link RichPresence} from the current state of this builder.
         *
         * @return The RichPresence built.
         */
        public RichPresence build()
        {
            return new RichPresence(state, details, startTimestamp, endTimestamp, 
                    largeImageKey, largeImageText, smallImageKey, smallImageText, 
                    partyId, partySize, partyMax, matchSecret, joinSecret, 
                    spectateSecret, instance, buttons);
        }

        /**
         * Adds a clickable button to the presence.
         *
         * @param label The label of the button.
         * @param url The url for the button to link to.
         *
         * @return This Builder.
         */
        public Builder addButton(String label, String url) {
            this.buttons.put(label, url);
            return this;
        }

        /**
         * Sets the state of the user's current party.
         *
         * @param state The state of the user's current party.
         *
         * @return This Builder.
         */
        public Builder setState(String state)
        {
            this.state = state;
            return this;
        }

        /**
         * Sets details of what the player is currently doing.
         *
         * @param details The details of what the player is currently doing.
         *
         * @return This Builder.
         */
        public Builder setDetails(String details)
        {
            this.details = details;
            return this;
        }

        /**
         * Sets the time that the player started a match or activity.
         *
         * @param startTimestamp The time the player started a match or activity.
         *
         * @return This Builder.
         */
        public Builder setStartTimestamp(OffsetDateTime startTimestamp)
        {
            this.startTimestamp = startTimestamp;
            return this;
        }

        /**
         * Sets the time that the player's current activity will end.
         *
         * @param endTimestamp The time the player's activity will end.
         *
         * @return This Builder.
         */
        public Builder setEndTimestamp(OffsetDateTime endTimestamp)
        {
            this.endTimestamp = endTimestamp;
            return this;
        }

        /**
         * Sets the key of the uploaded image for the large profile artwork, as well as
         * the text tooltip shown when a cursor hovers over it.
         *
         * <p>These can be configured in the <a href="https://discordapp.com/developers/applications/me">applications</a>
         * page on the discord website.
         *
         * @param largeImageKey A key to an image to display.
         * @param largeImageText Text displayed when a cursor hovers over the large image.
         *
         * @return This Builder.
         */
        public Builder setLargeImage(String largeImageKey, String largeImageText)
        {
            this.largeImageKey = largeImageKey;
            this.largeImageText = largeImageText;
            return this;
        }

        /**
         * Sets the key of the uploaded image for the large profile artwork.
         *
         * <p>These can be configured in the <a href="https://discordapp.com/developers/applications/me">applications</a>
         * page on the discord website.
         *
         * @param largeImageKey A key to an image to display.
         *
         * @return This Builder.
         */
        public Builder setLargeImage(String largeImageKey)
        {
            return setLargeImage(largeImageKey, null);
        }

        /**
         * Sets the key of the uploaded image for the small profile artwork, as well as
         * the text tooltip shown when a cursor hovers over it.
         *
         * <p>These can be configured in the <a href="https://discordapp.com/developers/applications/me">applications</a>
         * page on the discord website.
         *
         * @param smallImageKey A key to an image to display.
         * @param smallImageText Text displayed when a cursor hovers over the small image.
         *
         * @return This Builder.
         */
        public Builder setSmallImage(String smallImageKey, String smallImageText)
        {
            this.smallImageKey = smallImageKey;
            this.smallImageText = smallImageText;
            return this;
        }

        /**
         * Sets the key of the uploaded image for the small profile artwork.
         *
         * <p>These can be configured in the <a href="https://discordapp.com/developers/applications/me">applications</a>
         * page on the discord website.
         *
         * @param smallImageKey A key to an image to display.
         *
         * @return This Builder.
         */
        public Builder setSmallImage(String smallImageKey)
        {
            return setSmallImage(smallImageKey, null);
        }

        /**
         * Sets party configurations for a team, lobby, or other form of group.
         *
         * <p>The {@code partyId} is ID of the player's party.
         * <br>The {@code partySize} is the current size of the player's party.
         * <br>The {@code partyMax} is the maximum number of player's allowed in the party.
         *
         * @param partyId The ID of the player's party.
         * @param partySize The current size of the player's party.
         * @param partyMax The maximum number of player's allowed in the party.
         *
         * @return This Builder.
         */
        public Builder setParty(String partyId, int partySize, int partyMax)
        {
            this.partyId = partyId;
            this.partySize = partySize;
            this.partyMax = partyMax;
            return this;
        }

        /**
         * Sets the unique hashed string for Spectate and Join.
         *
         * @param matchSecret The unique hashed string for Spectate and Join.
         *
         * @return This Builder.
         */
        public Builder setMatchSecret(String matchSecret)
        {
            this.matchSecret = matchSecret;
            return this;
        }

        /**
         * Sets the unique hashed string for chat invitations and Ask to Join.
         *
         * @param joinSecret The unique hashed string for chat invitations and Ask to Join.
         *
         * @return This Builder.
         */
        public Builder setJoinSecret(String joinSecret)
        {
            this.joinSecret = joinSecret;
            return this;
        }

        /**
         * Sets the unique hashed string for Spectate button.
         *
         * @param spectateSecret The unique hashed string for Spectate button.
         *
         * @return This Builder.
         */
        public Builder setSpectateSecret(String spectateSecret)
        {
            this.spectateSecret = spectateSecret;
            return this;
        }

        /**
         * Marks the {@link #setMatchSecret(String) matchSecret} as a game
         * session with a specific beginning and end.
         *
         * @param instance Whether or not the {@code matchSecret} is a game
         *                 with a specific beginning and end.
         *
         * @return This Builder.
         */
        public Builder setInstance(boolean instance)
        {
            this.instance = instance;
            return this;
        }
    }
}
