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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * This is the code YOU have to provide
 *
 * @param warehouse all the operations you should need
 */
public class Solution
{
  public String getParticipantName()
  {
    return "Michael Krickl";
  }

  public Institute getParticipantInstitution()
  {
    return Institute.Sonstige;
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

    while (warehouse.hasNextOrder()) {
      Order order = warehouse.nextOrder();
      List<Product> todo = order.getProducts().stream()
          .sorted(Comparator.comparingInt(Product::getWidth))
          .collect(Collectors.toList());

      System.out.println("Order: " + order.getCode());
      while (todo.size() > 0) {
        for (Product p : todo) {
          Location l = nearestLocationOf(p.getCode());
          if (l != null && robot.getRemainingLength() >= p.getLength()) {
            robot.pullFrom(l);
          }
        }

        List<Product> roboProducts = new ArrayList<>(robot.getCurrentProducts());
        for (Product p : roboProducts) {
          robot.pushTo(exitLocation);
          todo.remove(p);
        }
        if (roboProducts.size() > 0) {
          continue;
        }

        while (true) {
          if (entryLocation.getCurrentProducts().isEmpty())
            break;
          Product entryProduct = entryLocation.getCurrentProducts().get(0);
          if (robot.getRemainingLength() < entryProduct.getLength() || robot.getCurrentMaxWidth() > entryProduct.getWidth())
            break;
          robot.pullFrom(entryLocation);
        }
        while (!robot.getCurrentProducts().isEmpty()) {
          pushToNearestStorage();
        }
      }
    }
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

  private Location nearestLocationOf(String productCode)
  {
    List<Location> nearestLocations = storage.getAllLocations().stream()
        .sorted(Comparator.comparingDouble(this::distanceRobot))
        .collect(Collectors.toList());

    return nearestLocations.stream()
        .filter(l -> !l.getCurrentProducts().isEmpty())
        .filter(l -> l.getCurrentProducts().get(0).getCode().equals(productCode))
        .filter(l -> l != exitLocation)
        .findFirst()
        .orElse(null);
  }

  private void pushToNearestStorage() throws Exception
  {
    final Product product = robot.getCurrentProducts().get(0);
    List<Location> nearestLocations = storage.getAllLocations().stream()
        .sorted(Comparator.comparingDouble(this::distanceCenter))
        .collect(Collectors.toList());

    Location location = nearestLocations.stream()
        .filter(l -> (
                (l.getCurrentProducts().isEmpty())
                    ||
                    (!l.getCurrentProducts().isEmpty() &&
                        l.getCurrentProducts().get(0).getCode().equals(product.getCode()) &&
                        l.getRemainingLength() >= product.getLength())
            )
        )
        .filter(l -> l != entryLocation && l != exitLocation)
        .findFirst()
        .orElseThrow(RuntimeException::new);
    robot.pushTo(location);
  }

  private double distance(Location l1, Location l2)
  {
    return Math.sqrt(Math.pow(l1.getLevel() - l2.getLevel(), 2) + Math.pow(l1.getPosition() - l2.getPosition(), 2));
  }

  private double distanceRobot(Location l)
  {
    return distance(robot.getCurrentLocation(), l);
  }

  private double distanceCenter(Location l)
  {
    try {
      return distance(storage.getLocation(0, 13), l);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // ----------------------------------------------------------------------------
}
