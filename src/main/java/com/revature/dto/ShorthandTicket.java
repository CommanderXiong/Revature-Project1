package com.revature.dto;

import java.util.Date;

import com.revature.model.Status;
import com.revature.model.Ticket;
import com.revature.model.Type;

public class ShorthandTicket {
	private int id;
	private double amount;
	private int authorId;
	private int resolverId;
	private Status status;
	private Type type;
	private Date issued;
	private Date resolved;
	
	public ShorthandTicket(Ticket ticket) {
		super();
		this.id = ticket.getId();
		this.amount = ticket.getAmount();
		this.authorId = ticket.getAuthor().getId();
		if (ticket.getResolver() != null) {
			this.resolverId = ticket.getResolver().getId();
			this.resolved = ticket.getResolved();
		}
		this.status = ticket.getStatus();
		this.type = ticket.getType();
		this.issued = ticket.getIssued();
	}
	
	public ShorthandTicket(int id, double amount, int authorId, int resolverId, Status status, Type type, Date issued,
			Date resolved) {
		super();
		this.id = id;
		this.amount = amount;
		this.authorId = authorId;
		this.resolverId = resolverId;
		this.status = status;
		this.type = type;
		this.issued = issued;
		this.resolved = resolved;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}

	public int getResolverId() {
		return resolverId;
	}

	public void setResolverId(int resolverId) {
		this.resolverId = resolverId;
	}

	public String getStatus() {
		return status.getStatus();
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getType() {
		return type.getType();
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Date getIssued() {
		return issued;
	}

	public void setIssued(Date issued) {
		this.issued = issued;
	}

	public Date getResolved() {
		return resolved;
	}

	public void setResolved(Date resolved) {
		this.resolved = resolved;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + authorId;
		result = prime * result + id;
		result = prime * result + ((issued == null) ? 0 : issued.hashCode());
		result = prime * result + ((resolved == null) ? 0 : resolved.hashCode());
		result = prime * result + resolverId;
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShorthandTicket other = (ShorthandTicket) obj;
		if (Double.doubleToLongBits(amount) != Double.doubleToLongBits(other.amount))
			return false;
		if (authorId != other.authorId)
			return false;
		if (id != other.id)
			return false;
		if (issued == null) {
			if (other.issued != null)
				return false;
		} else if (!issued.equals(other.issued))
			return false;
		if (resolved == null) {
			if (other.resolved != null)
				return false;
		} else if (!resolved.equals(other.resolved))
			return false;
		if (resolverId != other.resolverId)
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ShorthandTicket [id=" + id + ", amount=" + amount + ", authorId=" + authorId + ", resolverId="
				+ resolverId + ", status=" + status + ", type=" + type + ", issued=" + issued + ", resolved=" + resolved
				+ "]";
	}

}
