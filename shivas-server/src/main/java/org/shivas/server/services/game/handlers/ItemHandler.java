package org.shivas.server.services.game.handlers;

import org.shivas.protocol.client.enums.ItemPositionEnum;
import org.shivas.protocol.client.formatters.ItemGameMessageFormatter;
import org.shivas.server.database.models.GameItem;
import org.shivas.server.services.AbstractBaseHandler;
import org.shivas.server.services.CriticalException;
import org.shivas.server.services.game.GameClient;

public class ItemHandler extends AbstractBaseHandler<GameClient> {

	public ItemHandler(GameClient client) {
		super(client);
	}

	@Override
	public void init() throws Exception {
	}

	@Override
	public void onClosed() {
	}

	@Override
	public void handle(String message) throws Exception {
		String[] args;
		switch (message.charAt(1)) {
		case 'd':
			args = message.substring(2).split("\\|");
			parseDeleteMessage(
					client.player().getBag().get(Long.parseLong(args[0])),
					Integer.parseInt(args[1])
			);
			break;
			
		case 'M':
			args = message.substring(2).split("\\|");
			parseMoveMessage(
					client.player().getBag().get(Long.parseLong(args[0])),
					ItemPositionEnum.valueOf(Integer.parseInt(args[1]))
			);
			break;
		}
	}

	private void parseDeleteMessage(GameItem item, int quantity) throws CriticalException {
		if (quantity > item.getQuantity()) {
			throw new CriticalException("not enough quantity");
		} else if (quantity < item.getQuantity()) {
			item.minusQuantity(quantity);
			
			client.write(ItemGameMessageFormatter.quantityMessage(item.id(), item.getQuantity()));
		} else {
			client.player().getBag().delete(item);
			
			client.write(ItemGameMessageFormatter.deleteMessage(item.id()));
		}
		
		client.write(client.player().getStats().packet());
	}

	private void parseMoveMessage(GameItem item, ItemPositionEnum position) {
		
	}

}