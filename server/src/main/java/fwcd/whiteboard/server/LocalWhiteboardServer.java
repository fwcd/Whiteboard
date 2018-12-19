package fwcd.whiteboard.server;

import java.util.Set;
import java.util.function.Consumer;

import fwcd.whiteboard.protocol.dispatch.WhiteboardClient;
import fwcd.whiteboard.protocol.dispatch.WhiteboardServer;
import fwcd.whiteboard.protocol.event.UpdateAllItemsEvent;
import fwcd.whiteboard.protocol.request.AddItemsRequest;
import fwcd.whiteboard.protocol.request.GetAllItemsRequest;
import fwcd.whiteboard.protocol.request.Request;
import fwcd.whiteboard.protocol.request.SetAllItemsRequest;

public class LocalWhiteboardServer implements WhiteboardServer {
	private final Set<ClientConnection> activeConnections;
	private final ServerWhiteboardModel model = new ServerWhiteboardModel();
	
	public LocalWhiteboardServer(Set<ClientConnection> activeConnections) {
		this.activeConnections = activeConnections;
		
		model.getAddListeners().add(event -> forEachClient(c -> c.addItems(event)));
		model.getUpdateAllListeners().add(event -> forEachClient(c -> c.updateAllItems(event)));
	}
	
	@Override
	public void addItems(AddItemsRequest request) {
		synchronized (model) {
			model.addItems(request.getAddedItems());
		}
	}
	
	@Override
	public void getAllItems(GetAllItemsRequest request) {
		synchronized (model) {
			forEachClient(c -> c.updateAllItems(new UpdateAllItemsEvent(model.getItems())));
		}
	}
	
	@Override
	public void setAllItems(SetAllItemsRequest request) {
		synchronized (model) {
			model.setAllItems(request.getItems());
		}
	}
	
	private void forEachClient(Consumer<WhiteboardClient> consumer) {
		synchronized (activeConnections) {
			for (ClientConnection connection : activeConnections) {
				consumer.accept(connection.getClientProxy());
			}
		}
	}
	
	@Override
	public void otherRequest(Request request) {
		System.out.println("Unknown request: " + request); // TODO: Proper logging
	}
}