package de.rincewind.listener.dyn;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.RegisteredListener;

import de.rincewind.plugin.Main;

public abstract class DefaultListener implements Listener {
	
	private final List<String> enabledBundles;
	
	private final HandlerList list;
	
	private final Class<? extends DefaultListener> clazz;
	
	public DefaultListener(Class<? extends DefaultListener> clazz, HandlerList list) {
		this.enabledBundles = new ArrayList<>();
		this.list = list;
		this.clazz = clazz;
		
		this.list.register(new RegisteredListener(this, new EventExecutor() {

			@Override
			public void execute(Listener paramListener, Event paramEvent) throws EventException {
				DefaultListener.this.onFire(paramEvent);
			}
			
		}, EventPriority.HIGH, Main.getInstance(), false));
	}
	
	private void onFire(Event event) {
		for (Method method : this.clazz.getDeclaredMethods()) {
			ListenerBundle annot = method.getAnnotation(ListenerBundle.class);
			if (annot != null) {
				String[] listenerBundleNames = annot.name();
				for (String name : listenerBundleNames) {
					if (this.enabledBundles.contains(name)) {
						if (method.getParameters().length >= 1) {
							if (Modifier.isStatic(method.getModifiers())) {
								if (Event.class.isAssignableFrom(method.getParameters()[0].getType())) {
									if (method.getParameters()[0].getType().equals(event.getClass())) {
										try {
											method.setAccessible(true);
											method.invoke(null, new Object[] { event });
											break;
										} catch (IllegalAccessException e) {
											e.printStackTrace();
										} catch (IllegalArgumentException e) {
											e.printStackTrace();
										} catch (InvocationTargetException e) {
											e.printStackTrace();
										}
									}
								} else {
									// TODO
								}
							} else {
								// TODO
							}
						} else {
							// TODO
						}
					}
				}
			}
		}
	}
	
	public final void enableBundle(String name) {
		this.enabledBundles.add(name);
	}
	
	public final void disableBundle(String name) {
		this.enabledBundles.remove(name);
	}
	
	public final List<String> getEnabledBundles() {
		return this.enabledBundles;
	}
	
}
