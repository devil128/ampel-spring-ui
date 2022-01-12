package de.projectdw.springfxui.data;

import lombok.Data;

@Data
/**
 * helper class to save the state of an available network
 */
public class NetworkPair {
    String network;
    boolean online;
}

