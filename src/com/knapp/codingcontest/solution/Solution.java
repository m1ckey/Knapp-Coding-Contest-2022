/* -*- java -*-
# =========================================================================== #
#                                                                             #
#                         Copyright (C) KNAPP AG                              #
#                                                                             #
#       The copyright to the computer program(s) herein is the property       #
#       of Knapp.  The program(s) may be used   and/or copied only with       #
#       the  written permission of  Knapp  or in  accordance  with  the       #
#       terms and conditions stipulated in the agreement/contract under       #
#       which the program(s) have been supplied.                              #
#                                                                             #
# =========================================================================== #
*/

package com.knapp.codingcontest.solution;

import java.util.List;

import com.knapp.codingcontest.data.InputData;
import com.knapp.codingcontest.data.Institute;
import com.knapp.codingcontest.data.Location;
import com.knapp.codingcontest.data.Order;
import com.knapp.codingcontest.data.Product;
import com.knapp.codingcontest.warehouse.Robot;
import com.knapp.codingcontest.warehouse.Storage;
import com.knapp.codingcontest.warehouse.Warehouse;
import com.knapp.codingcontest.warehouse.WarehouseInfo;

/**
 * This is the code YOU have to provide
 *
 * @param warehouse all the operations you should need
 */
public class Solution {
  public String getParticipantName() {
    return ; // TODO: return your name
  }

  public Institute getParticipantInstitution() {
    return Institute. ; // TODO: return the Id of your institute - please refer to the hand-out
  }

  // ----------------------------------------------------------------------------

  protected final InputData input;
  protected final Warehouse warehouse;

  protected final Storage storage;
  protected final Location entryLocation;
  protected final Location exitLocation;
  protected final Robot robot;

  // ----------------------------------------------------------------------------

  public Solution(final Warehouse warehouse, final InputData input) {
    this.input = input;
    this.warehouse = warehouse;

    storage = warehouse.getStorage();
    entryLocation = storage.getEntryLocation();
    exitLocation = storage.getExitLocation();
    robot = storage.getRobot();

    // TODO: prepare data structures
  }

  // ----------------------------------------------------------------------------

  /**
   * The main entry-point
   */
  public void run() throws Exception {
    // TODO: make calls to API (see below)
  }

  // ----------------------------------------------------------------------------
  // ----------------------------------------------------------------------------

  /**
   * Just for documentation purposes.
   *
   * Method may be removed without any side-effects
   *
   *   divided into 4 sections
   *
   *     <li><em>input methods</em>
   *
   *     <li><em>main interaction methods</em>
   *         - these methods are the ones that make (explicit) changes to the warehouse
   *
   *     <li><em>information</em>
   *         - information you might need for your solution
   *
   *     <li><em>additional information</em>
   *         - various other infos: statistics, information about (current) costs, ...
   *
   */
  @SuppressWarnings("unused")
  private void apis() throws Exception {
    // ----- input -----

    final List<Product> allProductsAtEntry = input.getAllProductsAtEntry();
    final List<Order> allOrders = input.getAllOrders();

    final List<Product> remainingProducts = warehouse.getRemainingProductsAtEntry();
    final List<Order> remainingOrders = warehouse.getRemainingOrders();

    final Location location0 = storage.getLocation(0, 0);
    final List<Location> allLocations = storage.getAllLocations();

    // ----- main interaction methods -----

    Location location;

    location = entryLocation;
    robot.pullFrom(location);

    location = exitLocation;
    robot.pushTo(location);

    final Order order = warehouse.nextOrder();

    // ----- information -----

    final Product product = order.getProducts().get(0);

    location.getType();
    location.getLevel();
    location.getPosition();
    location.getLength();
    location.getRemainingLength();
    location.getCurrentProducts();

    final Location lamLocation = robot.getCurrentLocation();
    robot.getCurrentProducts();
    robot.getLength();
    robot.getRemainingLength();
    robot.getCurrentMaxWidth();

    // ----- additional information -----
    final WarehouseInfo info = warehouse.getInfoSnapshot();
  }

  // ----------------------------------------------------------------------------
}
