package de.rincewind.command.dyn;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;

public final class ReflectionUtil {
	
	private final static ReflectionUtil INSTANCE = new ReflectionUtil();
	
	public static ReflectionUtil instance() {
		return ReflectionUtil.INSTANCE;
	}
	
	public final String VERSION;
	
	public final Class<?> CLASS_CRAFTSERVER;
	public final Class<?> CLASS_SIMPLECOMMANDMAP;
	public final Class<?> CLASS_PACKET_TITLE;
	public final Class<?> CLASS_TITLEACTION;
	public final Class<?> CLASS_CHATBASE;
	public final Class<?> CLASS_CHATSERIALIZER;
	public final Class<?> CLASS_CRAFTPLAYER;
	public final Class<?> CLASS_ENTITYPLAYER;
	public final Class<?> CLASS_PACKET;
	public final Class<?> CLASS_PLAYERCONNECTION;
	public final Class<?> CLASS_PACKET_CHAT;
	public final Class<?> CLASS_PACKET_TABLIST;
	
	public final Constructor<?> CONSTRUCTOR_PACKET_TITLE_TITLE;
	public final Constructor<?> CONSTRUCTOR_PACKET_TITLE_TIMES;
	public final Constructor<?> CONSTRUCTOR_PACKET_CHAT;
	
	public final Field FIELD_COMMANDMAP;
	public final Field FIELD_COMMANDS;
	public final Field FIELD_CONNECTION;
	public final Field FIELD_TABLIST_HEADER;
	public final Field FIELD_TABLIST_FOOTER;
	
	public final Method METHOD_A;
	public final Method METHOD_GETHANDLE;
	public final Method METHOD_SENDPACKET;
	
	public final SimpleCommandMap COMMAND_MAP;
	public final Map<String, Command> COMMANDS;
	
	@SuppressWarnings("unchecked")
	private ReflectionUtil() {
		this.VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		
		this.CLASS_CRAFTSERVER = this.getClass("org.bukkit.craftbukkit." + this.VERSION + ".CraftServer");
		this.CLASS_SIMPLECOMMANDMAP = this.getClass("org.bukkit.command.SimpleCommandMap");
		this.CLASS_PACKET_TITLE = this.getNMSClass("PacketPlayOutTitle");
		this.CLASS_TITLEACTION = this.getNMSClass("PacketPlayOutTitle$EnumTitleAction");
		this.CLASS_CHATBASE = this.getNMSClass("IChatBaseComponent");
		this.CLASS_CHATSERIALIZER = this.getNMSClass("IChatBaseComponent$ChatSerializer");
		this.CLASS_CRAFTPLAYER = this.getClass("org.bukkit.craftbukkit." + this.VERSION + ".entity.CraftPlayer");
		this.CLASS_ENTITYPLAYER = this.getNMSClass("EntityPlayer");
		this.CLASS_PACKET = this.getNMSClass("Packet");
		this.CLASS_PLAYERCONNECTION = this.getNMSClass("PlayerConnection");
		this.CLASS_PACKET_CHAT = this.getNMSClass("PacketPlayOutChat");
		this.CLASS_PACKET_TABLIST = this.getNMSClass("PacketPlayOutPlayerListHeaderFooter");
		
		this.CONSTRUCTOR_PACKET_TITLE_TITLE = this.getConstructor(this.CLASS_PACKET_TITLE, new Class<?>[] { this.CLASS_TITLEACTION, this.CLASS_CHATBASE });
		this.CONSTRUCTOR_PACKET_TITLE_TIMES = this.getConstructor(this.CLASS_PACKET_TITLE, new Class<?>[] { int.class, int.class, int.class });
		this.CONSTRUCTOR_PACKET_CHAT = this.getConstructor(this.CLASS_PACKET_CHAT, new Class<?>[] { this.CLASS_CHATBASE, byte.class });
		
		this.FIELD_COMMANDMAP = this.getDeclaredField(this.CLASS_CRAFTSERVER, "commandMap");
		this.FIELD_COMMANDS = this.getDeclaredField(this.CLASS_SIMPLECOMMANDMAP, "knownCommands");
		this.FIELD_CONNECTION = this.getDeclaredField(this.CLASS_ENTITYPLAYER, "playerConnection");
		this.FIELD_TABLIST_HEADER = this.getDeclaredField(this.CLASS_PACKET_TABLIST, "a");
		this.FIELD_TABLIST_FOOTER = this.getDeclaredField(this.CLASS_PACKET_TABLIST, "b");
		
		this.METHOD_A = this.getMethod(this.CLASS_CHATSERIALIZER, "a", new Class<?>[] { String.class });
		this.METHOD_GETHANDLE = this.getMethod(this.CLASS_CRAFTPLAYER, "getHandle", new Class<?>[0]);
		this.METHOD_SENDPACKET = this.getMethod(this.CLASS_PLAYERCONNECTION, "sendPacket", new Class<?>[] { this.CLASS_PACKET });
		
		this.accessFields();
		this.accessMethods();
		
		this.COMMAND_MAP = (SimpleCommandMap) this.createObject(this.FIELD_COMMANDMAP, Bukkit.getServer());
		this.COMMANDS =  (Map<String, Command>) this.createObject(this.FIELD_COMMANDS, this.COMMAND_MAP);
	}
	
	private void accessFields() {
		this.FIELD_COMMANDMAP.setAccessible(true);
		this.FIELD_COMMANDS.setAccessible(true);
		this.FIELD_CONNECTION.setAccessible(true);
		this.FIELD_TABLIST_HEADER.setAccessible(true);
		this.FIELD_TABLIST_FOOTER.setAccessible(true);
	}
	
	private void accessMethods() {
		this.METHOD_A.setAccessible(true);
		this.METHOD_GETHANDLE.setAccessible(true);
		this.METHOD_SENDPACKET.setAccessible(true);
	}
	
	public Class<?> getClass(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new ReflectException("Could not get the class '" + className + "'!");
		} 
	}
	
	public Class<?> getNMSClass(String className) {
		try {
			return Class.forName("net.minecraft.server." + this.VERSION + "." + className);
		} catch (ClassNotFoundException e) {
			throw new ReflectException("Could not get the class '" + className + "'!");
		} 
	}
	
	public Field getDeclaredField(Class<?> clazz, String name) {
		try {
			return clazz.getDeclaredField(name);
		} catch (NoSuchFieldException e) {
			throw new ReflectException("Could not find the field '" + name + "' in class '" + clazz.getName() + "'!");
		} catch (SecurityException e) {
			throw new ReflectException("A security error occured!");
		} 
	}
	
	public Method getMethod(Class<?> clazz, String name, Class<?>[] array) {
		try {
			return clazz.getMethod(name, array);
		} catch (NoSuchMethodException e) {
			throw new ReflectException("Could not find the method '" + name + "' in class '" + clazz.getName() + "'!");
		} catch (SecurityException e) {
			throw new ReflectException("A security error occured!");
		}
	}
	
	public Constructor<?> getConstructor(Class<?> clazz, Class<?>[] array) {
		try {
			return clazz.getConstructor(array);
		} catch (NoSuchMethodException e) {
			throw new ReflectException("Could not find the constructor '" + Arrays.asList(array).toString() + "' in class '" + clazz.getName() + "'!");
		} catch (SecurityException e) {
			throw new ReflectException("A security error occured!");
		}
	}
	
	public Object createObject(Field field) {
		return this.createObject(field, null);
	}
	
	public Object createObject(Field field, Object from) {
		try {
			return field.get(from);
		} catch (IllegalArgumentException e) {
			throw new ReflectException("Could not get the field '" + field.getName() + "' of '" + from.getClass().getName() + "'!");
		} catch (IllegalAccessException e) {
			throw new ReflectException("Could not access the field '" + field.getName() + "'!");
		}
	}
	
	public Object createObject(Method method, Object from) {
		return this.createObject(method, from, new Object[0]);
	}
	
	public Object createStaticObject(Method method, Object... parameters) {
		return this.createObject(method, null, parameters);
	}
	
	public Object createStaticObject(Method method) {
		return this.createObject(method, null, new Object[0]);
	}
	
	public Object createObject(Method method, Object from, Object... parameters) {
		try {
			return method.invoke(from, parameters);
		} catch (IllegalAccessException e) {
			throw new ReflectException("Could not access the method '" + method.getName() + "' of '" + from.getClass().getName() + "'!");
		} catch (InvocationTargetException e) {
			throw new ReflectException("An error occured while invoking the method '" + method.getName() + "' of '" + from.getClass().getName() + "'!");
		}
	}
	
	public Object createObject(Constructor<?> from) {
		return this.createObject(from, new Object[0]);
	}
	
	public Object createObject(Constructor<?> from, Object... parameters) {
		try {
			return from.newInstance(parameters);
		} catch (InstantiationException e) {
			throw new ReflectException("An error occured while invoking the constructor '" + Arrays.asList(parameters).toString() + "'!");
		} catch (IllegalAccessException e) {
			throw new ReflectException("Could not access the constructor '" + Arrays.asList(parameters).toString() + "'!");
		} catch (IllegalArgumentException e) {
			throw new ReflectException("An error occured while invoking the constructor '" + Arrays.asList(parameters).toString() + "'!");
		} catch (InvocationTargetException e) {
			throw new ReflectException("An error occured while invoking the constructor '" + Arrays.asList(parameters).toString() + "'!");
		}
	}
	
	public Object createObject(Class<?> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			throw new ReflectException("An error occured while creating an instance of the class '" + clazz.getName() + "'!");
		} catch (IllegalAccessException e) {
			throw new ReflectException("An error occured while creating an instance of the class '" + clazz.getName() + "'!");
		}
	}
	
	public void invokeMethod(Method method, Object from) {
		this.invokeMethod(method, from, new Object[0]);
	}
	
	public void invokeStaticMethod(Method method, Object... parameters) {
		this.invokeMethod(method, null, parameters);
	}
	
	public void invokeStaticMethod(Method method) {
		this.invokeMethod(method, null, new Object[0]);
	}
	
	public void invokeMethod(Method method, Object from, Object... parameters) {
		try {
			method.invoke(from, parameters);
		} catch (IllegalAccessException e) {
			throw new ReflectException("Could not access the method '" + method.getName() + "' of '" + from.getClass().getName() + "'!");
		} catch (InvocationTargetException e) {
			throw new ReflectException("An error occured while invoking the method '" + method.getName() + "' of '" + from.getClass().getName() + "'!");
		}
	}
	
	public void setValue(Field field, Object from, Object value) {
		try {
			field.set(from, value);
		} catch (IllegalArgumentException e) {
			throw new ReflectException("Could not set the field '" + field.getName() + "' of '" + from.getClass().getName() + "' to '" + value.toString() + "'!");
		} catch (IllegalAccessException e) {
			throw new ReflectException("Could not access the field '" + field.getName() + "'!");
		}
	}
	
	public void setValue(Field field, Object value) {
		this.setValue(field, null, value);
	}
	
	public boolean check() {
		return this.COMMAND_MAP != null && this.COMMANDS != null;
	}
	
	@SuppressWarnings("serial")
	public static class ReflectException extends NullPointerException {
		
		public ReflectException(String s) {
			super(s);
		}
		
	}
	
}
