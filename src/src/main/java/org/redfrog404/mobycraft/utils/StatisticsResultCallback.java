package org.redfrog404.mobycraft.utils;

import static org.redfrog404.mobycraft.commands.BasicDockerCommands.printBasicContainerInformation;
import static org.redfrog404.mobycraft.commands.ContainerListCommands.getBoxContainerWithID;
import static org.redfrog404.mobycraft.commands.ContainerListCommands.getFromAllWithName;
import static org.redfrog404.mobycraft.commands.MainCommand.arg1;
import static org.redfrog404.mobycraft.utils.SendMessagesToCommandSender.sendMessage;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedHashMap;

import net.minecraft.util.EnumChatFormatting;

import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Statistics;
import com.github.dockerjava.core.async.ResultCallbackTemplate;

public class StatisticsResultCallback extends
		ResultCallbackTemplate<StatisticsResultCallback, Statistics> {

	@Override
	public void onNext(Statistics stats) {
		BoxContainer boxContainer = getBoxContainerWithID(arg1);
		Container container = getFromAllWithName(boxContainer.getName());

		NumberFormat formatter = new DecimalFormat("#0.00");

		printBasicContainerInformation(boxContainer, container);
		sendMessage(EnumChatFormatting.RED
				+ ""
				+ EnumChatFormatting.BOLD
				+ "Memory Usage: "
				+ EnumChatFormatting.RESET
				+ formatter.format((((Integer) stats.getMemoryStats().get(
						"usage")).doubleValue() / ((Integer) stats
						.getMemoryStats().get("limit")).doubleValue()) * 100D)
				+ "%");
		Object totalUsage = ((LinkedHashMap) stats.getCpuStats().get(
				"cpu_usage")).get("total_usage");
		double totalUsageDouble = 0D;
		if (totalUsage instanceof Integer) {
			totalUsageDouble = ((Integer) totalUsage).doubleValue();
		} else if (totalUsage instanceof Long) {
			totalUsageDouble = ((Long) totalUsage).doubleValue();
		}
		sendMessage(EnumChatFormatting.GRAY
				+ ""
				+ EnumChatFormatting.BOLD
				+ "CPU Usage: "
				+ EnumChatFormatting.RESET
				+ formatter.format(totalUsageDouble
						/ ((Long) stats.getCpuStats().get("system_cpu_usage"))
								.doubleValue() * 100D) + "%");
		try {
			super.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
