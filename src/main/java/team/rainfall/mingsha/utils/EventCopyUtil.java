package team.rainfall.mingsha.utils;

import age.of.civilizations2.jakowski.lukasz.CFG;
import age.of.civilizations2.jakowski.lukasz.Event_GameData;
import age.of.civilizations2.jakowski.lukasz.EventsJ;
import com.badlogic.gdx.utils.Json;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;

/**
 * 事件复制。
 *
 * 原版 Menu_CreateScenario_Events_List 的复制流程有两个问题：
 * 1. dc() 用 Java 序列化深拷贝，失败时只返回 null；调用方却不检查，
 *    addEvent2(null) 静默什么都不做，随后仍把编辑目标指向"最后一个事件"
 *    并弹出 "Copy: Done"。若被复制的事件正好是最后一个，编辑器打开的就是
 *    原事件本身 —— 表现为"复制出来的事件和原事件是同一个（id 相同）"。
 * 2. 新事件的 tag（事件唯一 id）完全依赖 addEvent2 内部重新生成，
 *    没有任何去重校验。
 *
 * 这里把复制做成：深拷贝（序列化失败则退回游戏自己存档用的 JSON 序列化）
 * → 显式分配一个与现有事件都不冲突的新 tag → 确认已入表才打开编辑器，
 * 失败时给出失败提示而不是假的 "Done"。
 */
public class EventCopyUtil {

    /** 生成唯一 tag 时的兜底计数器 */
    private static int iFallbackCounter = 0;

    /**
     * 复制指定事件，成功后把编辑器指向副本。
     *
     * @param nEventID 源事件在 EventsManager 里的下标
     * @return 是否复制成功
     */
    public static boolean copyEvent(int nEventID) {
        if (CFG.eventsManager == null) {
            return false;
        }
        if (nEventID < 0 || nEventID >= CFG.eventsManager.getEventsSize()) {
            copyFailed(null);
            return false;
        }

        Event_GameData tSource = CFG.eventsManager.getEvent(nEventID);
        if (tSource == null) {
            copyFailed(null);
            return false;
        }

        Event_GameData tCopy = deepCopy(tSource);
        // tCopy == tSource 说明根本没拷出新对象，绝不能继续，否则会改到原事件
        if (tCopy == null || tCopy == tSource) {
            copyFailed(null);
            return false;
        }

        tCopy.setEventTag(newUniqueEventTag());
        CFG.eventsManager.addEvent(tCopy);

        int tNewID = CFG.eventsManager.getEventsSize() - 1;
        if (tNewID < 0 || CFG.eventsManager.getEvent(tNewID) != tCopy) {
            copyFailed(null);
            return false;
        }

        CFG.eventsManager.createEvent_EditEventID = tNewID;
        CFG.eventsManager.createScenarioEvents = tCopy;
        CFG.menus.setVisibleCreateScenario_Events_Edit(true);
        CFG.toastM.addM(CFG.lang.get("Copy") + ": " + CFG.lang.get("Done"), CFG.COLOR_POSITIVE);
        return true;
    }

    /** 先试 Java 序列化，失败再退回 JSON（游戏保存事件用的就是 JSON） */
    public static Event_GameData deepCopy(Event_GameData nSource) {
        Event_GameData tCopy = copyBySerialization(nSource);
        if (tCopy == null) {
            tCopy = copyByJson(nSource);
        }

        return tCopy;
    }

    /** 生成一个与现有所有事件都不重复的 tag */
    public static String newUniqueEventTag() {
        for (int i = 0; i < 64; i++) {
            String tTag = System.currentTimeMillis() + CFG.extraRandomTag();
            if (!eventTagExists(tTag)) {
                return tTag;
            }
        }

        return System.currentTimeMillis() + CFG.extraRandomTag() + "x" + iFallbackCounter++;
    }

    private static boolean eventTagExists(String nTag) {
        for (int i = 0; i < CFG.eventsManager.getEventsSize(); i++) {
            Event_GameData tEvent = CFG.eventsManager.getEvent(i);
            if (tEvent != null && nTag.equals(tEvent.getEventTag())) {
                return true;
            }
        }

        return false;
    }

    private static void copyFailed(Throwable nEx) {
        if (nEx != null) {
            CFG.exceptionStack(nEx);
        }
        CFG.toastM.addM(CFG.lang.get("Copy") + ": FAILED", CFG.COLOR_NEGATIVE_1);
    }

    private static Event_GameData copyBySerialization(Event_GameData nSource) {
        try {
            ByteArrayOutputStream tBytes = new ByteArrayOutputStream();
            ObjectOutputStream tOut = new ObjectOutputStream(tBytes);
            tOut.writeObject(nSource);
            tOut.flush();
            tOut.close();

            ObjectInputStream tIn = new ModObjectInputStream(new ByteArrayInputStream(tBytes.toByteArray()));
            Event_GameData tCopy = (Event_GameData) tIn.readObject();
            tIn.close();
            return tCopy;
        } catch (Throwable var5) {
            CFG.exceptionStack(var5);
            return null;
        }
    }

    private static Event_GameData copyByJson(Event_GameData nSource) {
        try {
            Json tJson = EventsJ.getJson();
            return tJson.fromJson(Event_GameData.class, tJson.prettyPrint(nSource));
        } catch (Throwable var3) {
            CFG.exceptionStack(var3);
            return null;
        }
    }

    /**
     * 默认的 ObjectInputStream 用 latestUserDefinedLoader 找类，在插件环境下
     * 可能看不到 mod 自己新增的 Event_Outcome_* / Event_Conditions_* 类，
     * 一旦解析失败整个拷贝就废了。这里显式按 插件 → 游戏 → 默认 的顺序找。
     */
    private static final class ModObjectInputStream extends ObjectInputStream {
        ModObjectInputStream(InputStream nIn) throws IOException {
            super(nIn);
        }

        @Override
        protected Class<?> resolveClass(ObjectStreamClass nDesc) throws IOException, ClassNotFoundException {
            try {
                return Class.forName(nDesc.getName(), false, EventCopyUtil.class.getClassLoader());
            } catch (ClassNotFoundException var3) {
                try {
                    return Class.forName(nDesc.getName(), false, Event_GameData.class.getClassLoader());
                } catch (ClassNotFoundException var4) {
                    return super.resolveClass(nDesc);
                }
            }
        }
    }
}
