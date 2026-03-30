package com.example.springtest.transaction;

import java.util.List;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class AfterCommitProbe {

	public void registerCallbacks(List<String> events) {
		events.add("register");

		TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
			@Override
			public void beforeCommit(boolean readOnly) {
				events.add("beforeCommit");
			}

			@Override
			public void beforeCompletion() {
				events.add("beforeCompletion");
			}

			@Override
			public void afterCommit() {
				events.add("afterCommit");
			}

			@Override
			public void afterCompletion(int status) {
				events.add("afterCompletion:" + toStatusLabel(status));
			}
		});

		events.add("registered");
	}

	private String toStatusLabel(int status) {
		return switch (status) {
			case TransactionSynchronization.STATUS_COMMITTED -> "COMMITTED";
			case TransactionSynchronization.STATUS_ROLLED_BACK -> "ROLLED_BACK";
			case TransactionSynchronization.STATUS_UNKNOWN -> "UNKNOWN";
			default -> "UNEXPECTED:" + status;
		};
	}
}