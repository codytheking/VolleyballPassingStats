//
//  PlayerTableViewController.swift
//  VolleyballPassingStats
//
//  Created by Cody King on 4/27/20.
//  Copyright © 2020 Cody J. King. All rights reserved.
//

import UIKit

class PlayerTableViewController: UITableViewController, UITabBarControllerDelegate, PlayerTableViewCellDelegate {
    

    // MARK: Properties
    
    static var players = [Player]()
    static var visiblePlayers = [Player]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Set nav bar title
        self.title = "Passing Stats"
        
        self.tabBarController?.delegate = self

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem
        
        for _ in 1...OptionsViewController.MAX_NUM_PLAYERS
        {
            guard let player = Player() else {
                fatalError("Unable to instantiate Player object.")
            }
            PlayerTableViewController.players.append(player)
            PlayerTableViewController.visiblePlayers.append(player)
        }
    }
    
    func tabBarController(_ tabBarController: UITabBarController, didSelect viewController: UIViewController) {
        let tabBarIndex = tabBarController.selectedIndex
        
        // tapped on tab 0 of Tab Bar
        if tabBarIndex == 0 {
            if OptionsViewController.clearAll {
                for p in PlayerTableViewController.players {
                    p.resetAll()
                    navigationItem.title = "Passing Stats"
                }
                OptionsViewController.clearAll = false
                tableView.reloadData()
            }
            
            if OptionsViewController.numPlayers != PlayerTableViewController.visiblePlayers.count {
                let num = OptionsViewController.numPlayers
                
                if num < PlayerTableViewController.visiblePlayers.count {
                    PlayerTableViewController.visiblePlayers.removeSubrange(num..<PlayerTableViewController.visiblePlayers.count)
                }
                else {
                    for i in PlayerTableViewController.visiblePlayers.count..<num {
                        PlayerTableViewController.visiblePlayers.append(PlayerTableViewController.players[i])
                    }
                }
                
                if getTotalPasses() == 0 {
                    navigationItem.title = "Passing Stats"
                }
                else {
                    navigationItem.title = "Group Average: \(PlayerTableViewController.getGroupAvg())"
                }
                
                tableView.reloadData()
            }
        }
    }

    // MARK: - Table view data source

    // Tells the table view how many sections to display.
    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    // Tells the table view how many rows to display in a given section.
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return PlayerTableViewController.visiblePlayers.count
    }

    // Configures and provides a cell to display for a given row.
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        // Table view cells are reused and should be dequeued using a cell identifier.
        let cellIdentifier = "PlayerTableViewCell"

        guard let cell = tableView.dequeueReusableCell(withIdentifier: cellIdentifier, for: indexPath) as? PlayerTableViewCell  else {
            fatalError("The dequeued cell is not an instance of PlayerTableViewCell.")
        }

        let player = PlayerTableViewController.visiblePlayers[indexPath.row]
        cell.nameTextField.text = player.name
        cell.statsLabel.text = player.getStatsText()
        
        if player.lastPasses.count > 0 {
            let lastPass = player.lastPasses[player.lastPasses.count - 1]
            cell.lastPassLabel.text = "Last pass: \(lastPass)"
        }
        else {
            cell.lastPassLabel.text = ""
        }
        
        // the 'self' here means the view controller, set view controller as the delegate
        cell.delegate = self
            
        return cell
    }
    
    

    /*
    // Override to support conditional editing of the table view.
    override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the specified item to be editable.
        return true
    }
    */

    /*
    // Override to support editing the table view.
    override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            // Delete the row from the data source
            tableView.deleteRows(at: [indexPath], with: .fade)
        } else if editingStyle == .insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        }    
    }
    */

    /*
    // Override to support rearranging the table view.
    override func tableView(_ tableView: UITableView, moveRowAt fromIndexPath: IndexPath, to: IndexPath) {

    }
    */

    /*
    // Override to support conditional rearranging of the table view.
    override func tableView(_ tableView: UITableView, canMoveRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the item to be re-orderable.
        return true
    }
    */

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */
    
    
    // check which button inside cell was tapped
    func playerTableViewCell(_ playerTableViewCell: PlayerTableViewCell, _ buttonIndex: Int, _ rowIndex: Int, _ task: String, _ name: String) {

        if task == "score" {
            PlayerTableViewController.players[rowIndex].values[buttonIndex] += 1
            PlayerTableViewController.players[rowIndex].lastPasses.append(buttonIndex)
            navigationItem.title = "Group Average: \(PlayerTableViewController.getGroupAvg())"
            tableView.reloadData()
        }
        else if task == "reset" {
            PlayerTableViewController.players[rowIndex].reset()
            
            if getTotalPasses() == 0 {
                navigationItem.title = "Passing Stats"
            }
            else {
                navigationItem.title = "Group Average: \(PlayerTableViewController.getGroupAvg())"
            }
            
            tableView.reloadData()
        }
        else if task == "undo", PlayerTableViewController.players[rowIndex].lastPasses.count > 0 {
            let hist = PlayerTableViewController.players[rowIndex].lastPasses
            let lastBtnIndex = hist[hist.count - 1]
            PlayerTableViewController.players[rowIndex].values[lastBtnIndex] -= 1
            PlayerTableViewController.players[rowIndex].lastPasses.remove(at: hist.count - 1)
            
            if getTotalPasses() == 0 {
                navigationItem.title = "Passing Stats"
            }
            else {
                navigationItem.title = "Group Average: \(PlayerTableViewController.getGroupAvg())"
            }
            
            tableView.reloadData()
        }
        else if task == "set name" {
            PlayerTableViewController.players[rowIndex].name = name
            PlayerTableViewController.visiblePlayers[rowIndex].name = name
        }
    }
    
    
    // MARK: Private Methods
    
    static func getGroupAvg() -> Double {
        if PlayerTableViewController.visiblePlayers.count == 0 {
            return 0.0
        }
        
        var sum = 0.0
        var totalPasses = 0
        
        for p in PlayerTableViewController.visiblePlayers {
            sum += p.getAvg() * Double(p.getPasses())
            totalPasses += p.getPasses()
        }
        
        return (round(sum / Double(totalPasses) * 1000) / 1000)
    }
    
    private func getTotalPasses() -> Int {
        var totalPasses = 0
        
        for p in PlayerTableViewController.visiblePlayers {
            totalPasses += p.getPasses()
        }
        
        return totalPasses
    }
}
