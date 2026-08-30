package team.rainfall.mingsha.utils;

import age.of.civilizations2.jakowski.lukasz.CFG;
import age.of.civilizations2.jakowski.lukasz.Event_GameData;

/**
 * Marker events, which the scenario event list has no business showing.
 * <p>
 * Features that need per-scenario data of their own — the starting national spirits, the
 * counter definitions — keep it inside a never-firing event, because that is the one
 * container the game's scenario serializer already saves and loads. They are storage, not
 * events: an author cannot usefully open, copy or delete one, and deleting it silently
 * throws that feature's data away.
 * <p>
 * They are told apart by a name prefix rather than by asking each feature in turn, so a
 * marker added later is hidden without touching the list menu again. Nothing stops an
 * author from naming an event this way by hand, which would hide it — the prefix is
 * deliberately odd enough that this is a fair trade for not having to enumerate.
 */
public class HiddenEvents {

    /** Prefix every mingsha marker event's name starts with. */
    private static final String MARKER_PREFIX = "$$MS";

    private HiddenEvents() {
    }

    public static boolean isHidden(Event_GameData nEvent) {
        try {
            return nEvent != null && nEvent.getEventName() != null && nEvent.getEventName().startsWith(MARKER_PREFIX);
        } catch (Exception e) {
            return false;
        }
    }

    /** How many events an author can actually see, for the list's own header count. */
    public static int visibleCount() {
        int tCount = 0;
        try {
            for (int i = 0; i < CFG.eventsManager.getEventsSize(); i++) {
                if (!isHidden(CFG.eventsManager.getEvent(i))) {
                    tCount++;
                }
            }
        } catch (Exception ignored) {
        }
        return tCount;
    }
}
