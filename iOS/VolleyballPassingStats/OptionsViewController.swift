//
//  OptionsViewController.swift
//  VolleyballPassingStats
//
//  Created by Cody King on 4/26/20.
//  Copyright © 2020 Cody J. King. All rights reserved.
//

import UIKit


class OptionsViewController: UIViewController {
    
    // MARK: Properties
    
    @IBOutlet weak var numPlayersTextField: UITextField!
    @IBOutlet weak var clearAllBtn: UIButton!
    @IBOutlet weak var numPlayersStepper: UIStepper!
    @IBOutlet weak var versionLabel: UILabel!
    @IBOutlet weak var exportBtn: UIButton!
    
    static var clearAll = false
    static var numPlayers = 20
    static let maxNumPlayers = 20
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        numPlayersStepper.maximumValue = Double(OptionsViewController.maxNumPlayers)
        numPlayersStepper.minimumValue = 1
        numPlayersStepper.stepValue = 1
        numPlayersStepper.value = Double(OptionsViewController.maxNumPlayers)
        
        let appVersion = Bundle.main.infoDictionary?["CFBundleShortVersionString"] as? String
        versionLabel.text = "Version: \(appVersion ?? "")"
    }

    
    // MARK: Actions
    
    @IBAction func changeNumPlayers(_ sender: UIStepper) {
        self.numPlayersTextField.text = Int(sender.value).description
        OptionsViewController.numPlayers = Int(sender.value)
    }

    @IBAction func clearAllData(_ sender: UIButton) {
        let refreshAlert = UIAlertController(title: "Refresh", message: "All data will be lost.", preferredStyle: UIAlertController.Style.alert)

        refreshAlert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { (action: UIAlertAction!) in
            OptionsViewController.clearAll = true
        }))

        refreshAlert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: { (action: UIAlertAction!) in
        }))

        present(refreshAlert, animated: true, completion: nil)
    }
    
    @IBAction func exportData(_ sender: UIButton) {
        //Set the default sharing message.
        var message = "Here are your stats for \n\n";
        message += "Group Average: ";
        message += "\(PlayerTableViewController.getGroupAvg()) \n\n";

        var i = 1
        for p in PlayerTableViewController.visiblePlayers {
            var name = p.name;

            if(name.count == 0) {
                name = "No Name (P\(i))";
            }

            message += name + "\n" + p.getStatsText() + "\n\n";
            
            i += 1
        }
        
        let objectsToShare = [message] as [Any]
        let activityVC = UIActivityViewController(activityItems: objectsToShare, applicationActivities: nil)
        activityVC.excludedActivityTypes = [UIActivity.ActivityType.addToReadingList]
        self.present(activityVC, animated: true, completion: nil)
    }
    
    @IBAction func nameHyperlink(_ sender: UIButton) {
        if let url = NSURL(string: "https://codytheking.github.io/") {
            
            let refreshAlert = UIAlertController(title: "Open Link", message: "Go to developer's website?", preferredStyle: UIAlertController.Style.alert)

            refreshAlert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { (action: UIAlertAction!) in
                UIApplication.shared.open(url as URL, options: [:], completionHandler: nil)
            }))

            refreshAlert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: { (action: UIAlertAction!) in
            }))

            present(refreshAlert, animated: true, completion: nil)
        }
    }
}
